package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import entity.Item;

public class MySQLConnection {
	private Connection conn;

	public MySQLConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(MySQLDBUtil.URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean addUser(String userId, String password, String firstname, String lastname) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}

		String sql = "INSERT IGNORE INTO users VALUES(?, ?, ?, ?)";

		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, password);
			statement.setString(3, firstname);
			statement.setString(4, lastname);

			// return true if successfully insert (executeUpdate returns row count inserted)
			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean verifyLogin(String userId, String password) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}

		String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();

			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getFullname(String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return "";
		}

		String name = "";
		String sql = "SELECT first_name, last_name FROM users WHERE user_id = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	public void setFavoriteItems(String userId, Item item) {
		// check if there is connection with db
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		String sql = "INSERT IGNORE INTO history (user_id, item_id) VALUES (?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId); // sanitize userId to prevent SQL injection
			statement.setInt(2, item.getItemId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveItem(Item item) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		String sql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			String instructions = MySQLDBUtil.concateStrings(item.getInstructions());

			statement.setInt(1, item.getItemId());
			statement.setString(2, item.getImageUrl());
			statement.setString(3, item.getTitle());
			statement.setString(4, instructions);
			statement.setString(5, item.getSourceUrl());
			statement.executeUpdate();

			sql = "INSERT IGNORE INTO ingredients VALUES(?, ?, ?, ?)";
			statement = conn.prepareStatement(sql);
			statement.setInt(1, item.getItemId());

			List<Double> amounts = item.getAmounts();
			List<String> units = item.getUnits();
			List<String> ingredients = item.getIngredients();

			for (int i = 0; i < amounts.size(); i++) {
				statement.setDouble(2, amounts.get(i));
				statement.setString(3, units.get(i));
				statement.setString(4, ingredients.get(i));
				statement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Set<Item> getItems() {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new HashSet<>();
		}
		Set<Item> items = new HashSet<>();

		String sql = "SELECT * FROM items";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				List<String> instructions = MySQLDBUtil.textToStrings(rs.getString("instructions"));
				int itemId = rs.getInt("item_id");
				items.add(Item.builder().itemId(itemId).imageUrl(rs.getString("image_url")).title(rs.getString("title"))
						.amounts(getAmounts(itemId)).units(getUnits(itemId)).ingredients(getIngredients(itemId))
						.instructions(instructions).sourceUrl(rs.getNString("source_url")).build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public void unsetFavoriteItems(String userId, int itemId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}
		String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setInt(2, itemId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Set<Integer> getFavoriteItemIds(String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new HashSet<>();
		}

		Set<Integer> favoriteItems = new HashSet<>();

		try {
			String sql = "SELECT item_id FROM history WHERE user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				int itemId = rs.getInt("item_id");
				favoriteItems.add(itemId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return favoriteItems;
	}

	public Set<Item> getFavoriteItems(String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new HashSet<>();
		}
		Set<Item> favoriteItems = new HashSet<>();
		Set<Integer> favoritesItemIds = getFavoriteItemIds(userId);

		String sql = "SELECT * FROM items WHERE item_id = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			for (Integer itemId : favoritesItemIds) {
				statement.setInt(1, itemId);
				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					List<String> instructions = MySQLDBUtil.textToStrings(rs.getString("instructions"));

					favoriteItems.add(Item.builder().itemId(rs.getInt("item_id")).imageUrl(rs.getString("image_url"))
							.title(rs.getString("title")).amounts(getAmounts(itemId)).units(getUnits(itemId))
							.ingredients(getIngredients(itemId)).instructions(instructions)
							.sourceUrl(rs.getNString("source_url")).build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return favoriteItems;
	}

	public List<Double> getAmounts(int itemId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new ArrayList<>();
		}
		List<Double> amounts = new ArrayList<>();

		String sql = "SELECT amount FROM ingredients WHERE item_id = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, itemId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Double amount = rs.getDouble("amount");
				amounts.add(amount);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amounts;
	}

	public List<String> getUnits(int itemId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new ArrayList<>();
		}
		List<String> units = new ArrayList<>();

		String sql = "SELECT unit FROM ingredients WHERE item_id = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, itemId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String ingredient = rs.getString("unit");
				units.add(ingredient);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return units;
	}

	public List<String> getIngredients(int itemId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new ArrayList<>();
		}
		List<String> ingredients = new ArrayList<>();

		String sql = "SELECT ingredient FROM ingredients WHERE item_id = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, itemId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String ingredient = rs.getString("ingredient").toLowerCase();
				ingredients.add(ingredient);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ingredients;
	}

	// For traning model
	public HashMap<String, Integer> getIngredientFreq() {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new HashMap<String, Integer>();
		}
		HashMap<String, Integer> freqMap = new HashMap<String, Integer>();

		String sql = "SELECT ingredient FROM ingredients";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String ingredient = rs.getString("ingredient").toLowerCase();
				if (!freqMap.containsKey(ingredient)) {
					freqMap.put(ingredient, 1);
				} else {
					freqMap.put(ingredient, freqMap.get(ingredient) + 1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return freqMap;
	}

	// For write recipe id & ingredients into csv file in Export class
	public HashMap<Integer, List<String>> getItemIdIngred() {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new HashMap<Integer, List<String>>();
		}

		HashMap<Integer, List<String>> id_ingred_map = new HashMap<Integer, List<String>>();
		String sql = "SELECT item_id FROM ingredients GROUP BY item_id";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			List<Integer> ids = new ArrayList<>();
			while (rs.next()) {
				int itemId = rs.getInt("item_id");
				ids.add(itemId);
			}
			for (int id : ids) {
				id_ingred_map.put(id, getIngredients(id));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id_ingred_map;
	}

	public void setFridge(String userId, String ingredient) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		String sql = "INSERT IGNORE INTO fridge VALUES (?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, userId);
			statement.setString(2, ingredient);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Item> getRecommendItems(List<Integer> itemIds) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new ArrayList<>();
		}
		List<Item> recommendItems = new ArrayList<>();

		String sql = "SELECT * FROM items WHERE item_id = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			for (Integer itemId : itemIds) {
				statement.setInt(1, itemId);
				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
//					List<String> instructions = MySQLDBUtil.textToStrings(rs.getString("instructions"));

					recommendItems.add(Item.builder().itemId(rs.getInt("item_id")).imageUrl(rs.getString("image_url"))
							.title(rs.getString("title")).amounts(null).units(null).ingredients(getIngredients(itemId))
							.instructions(null).sourceUrl(rs.getNString("source_url")).build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recommendItems;
	}

	public List<Integer> getTopNMatchIds(List<String> fridge, int topN) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new ArrayList<>();
		}

		List<Integer> idList = new ArrayList<>();
		String sql = "";
		int fridgeSize = fridge.size();
		int num = fridgeSize;
		int threshold = fridgeSize;

		if (fridgeSize == 0) {
			return new ArrayList<>();
		}
		StringBuilder param = new StringBuilder("'");

		for (int i = 0; i < fridgeSize - 1; i++) {
			param.append(fridge.get(i));
			param.append("|");
		}

		param.append(fridge.get(fridgeSize - 1));
		param.append("'");
		System.out.println("slq: " + param);
		try {
			if (fridgeSize < 5) {
				threshold += 5;
			} else if (fridgeSize < 3) {
				threshold += 9;
			}
			while (num > 0) {
				sql = "SELECT * " + "FROM ( SELECT item_id FROM ingredients GROUP BY item_id having count(*) <= "
						+ Integer.toString(threshold) + ") AS A "
						+ "JOIN ( SELECT item_id FROM ingredients WHERE ingredient RLIKE " + param
						+ " group by item_id having count(*) >= " + num + ") AS B " + "ON A.item_id=B.item_id";

				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet rs = statement.executeQuery();
				while (rs.next() && idList.size() < topN) {
					Integer id = rs.getInt("item_id");
					System.out.println("query result id: " + id);
					if (!idList.contains(id)) {
						idList.add(id);
					}
				}
				num--;
				System.out.println("num of match: " + num);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idList;
	}

	public void unsetFridge(String userId, String ingredient) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}
		String sql = "DELETE FROM fridge WHERE user_id = ? AND ingredient = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, ingredient);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<String> getFridge(String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new ArrayList<>();
		}

		List<String> fridgeStorage = new ArrayList<>();

		try {
			String sql = "SELECT ingredient FROM fridge WHERE user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String ingredient = rs.getString("ingredient").toLowerCase();
				fridgeStorage.add(ingredient);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fridgeStorage;
	}

	public Set<Item> getRandomItems() {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new HashSet<>();
		}

		Set<Item> randomItems = new HashSet<>();
		try {
			String sql = "SELECT * FROM items ORDER BY RAND() LIMIT 10";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				int itemId = rs.getInt("item_id");
				randomItems.add(Item.builder().itemId(itemId).imageUrl(rs.getString("image_url")).title(rs.getString("title"))
						.ingredients(getIngredients(itemId))
						.sourceUrl(rs.getNString("source_url")).build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return randomItems;
	}
}
