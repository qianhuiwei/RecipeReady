package entity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
// use lombok library to enable builder pattern for Item object
public class Item {
	private int itemId; // id
	private String imageUrl; // image
	private String title; // title
	private List<Double> amounts; // extendedIngredients->measures->us->amount
	private List<String> units; // extendedIngredients->measures->us->unitShort
	private List<String> ingredients; // extendedIngredients->name
	private List<String> instructions; // analyzedInstructions->steps->step
	private String sourceUrl;

	public JSONObject toJSONObject() {

		JSONObject obj = new JSONObject();
		obj.put("item_id", itemId);
		obj.put("image_url", imageUrl);
		obj.put("title", title);
		obj.put("amounts", new JSONArray(amounts));
		obj.put("units", new JSONArray(units));
		obj.put("ingredients", new JSONArray(ingredients));
		obj.put("instructions", new JSONArray(instructions));
		obj.put("source_url", sourceUrl);

		return obj;
	}
}
