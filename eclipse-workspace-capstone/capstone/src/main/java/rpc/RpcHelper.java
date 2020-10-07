package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Item;

public class RpcHelper {
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(array);
	}
	
	public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(obj);
	}
	
	// convert a JSONObject to Item Object
	public static Item parseFavoriteItem(JSONObject favoriteItem) {
		List<Double> amounts = new ArrayList<>();
		List<String> units = new ArrayList<>();
		List<String> ingredients = new ArrayList<>();
		List<String> instructions = new ArrayList<>();
		
		JSONArray array = favoriteItem.getJSONArray("amounts");
		for (int i = 0; i < array.length(); i++) {
			amounts.add(array.getDouble(i));
		}
		
		array = favoriteItem.getJSONArray("units");
		for (int i = 0; i < array.length(); i++) {
			units.add(array.getString(i));
		}
		
		array = favoriteItem.getJSONArray("ingredients");
		for (int i = 0; i < array.length(); i++) {
			ingredients.add(array.getString(i));
		}
		
		array = favoriteItem.getJSONArray("instructions");
		for (int i = 0; i < array.length(); i++) {
			instructions.add(array.getString(i));
		}

		return Item.builder()
				.itemId(favoriteItem.getInt("item_id"))
				.imageUrl(favoriteItem.getString("image_url"))
				.title(favoriteItem.getString("title"))
				.amounts(amounts)
				.units(units)
				.ingredients(ingredients)
				.instructions(instructions)
				.sourceUrl(favoriteItem.getString("source_url"))
				.build();
	}
}
