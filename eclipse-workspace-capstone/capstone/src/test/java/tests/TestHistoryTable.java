package tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import db.MySQLConnection;
import entity.Item;
import external.RecipeAPIClient;

public class TestHistoryTable {
	@Test
	public void test() {
		MySQLConnection connection = new MySQLConnection();
		connection.testMySQLConnection();
		
		// test setFavoriteItems, getFavoriteItemIds, and getFavoriteItems method
		RecipeAPIClient client = new RecipeAPIClient();
		int itemId = 100000;
		List<Item> itemList = client.searchSingle(itemId);
		Item item = itemList.get(0);
		String userId = "user1";
		
		connection.setFavoriteItems(userId, item);
		
		List<Integer> ids = (List<Integer>) connection.getFavoriteItemIds(userId);
		assertEquals(1, ids.size());
		assertEquals((Integer)itemId,(Integer)ids.get(0));
		
		List<Item> savedItems = connection.getFavoriteItems(userId);
		Item savedItem = savedItems.get(0);
		assertEquals(1, savedItems.size());
		assertEquals(itemId, savedItem.getItemId());
		
		System.out.println("setFavoriteItems() db operation checked");
		System.out.println("getFavoriteItemIds() db operation checked");
		System.out.println("getFavoriteItems() db operation checked");
		
		// test unsetFavoriteItems method
		connection.unsetFavoriteItems(userId, itemId);
		
		ids = (List<Integer>) connection.getFavoriteItemIds(userId);
		assertEquals(0, ids.size());
		
		savedItems = connection.getFavoriteItems(userId);
		assertEquals(0, savedItems.size());
		
		System.out.println("unsetFavoriteItems() db operation checked");
		
		connection.close();
	}

}
