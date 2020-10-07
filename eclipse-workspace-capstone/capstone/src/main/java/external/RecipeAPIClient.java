package external;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import entity.Item;

public class RecipeAPIClient {
	// URL to get recipe meta (ID, image, title)
	// This URL returns JSONObject
	private static final String URL_RECIPE_ID_TEMPLATE_MANY = "https://api.spoonacular.com/recipes/informationBulk"
			+ "?ids=%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s" + "&apiKey=7f7e3fa22e264910a6bb6f915107598b";
	
	private static final String URL_RECIPE_ID_TEMPLATE_FEW = "https://api.spoonacular.com/recipes/informationBulk"
			+ "?ids=%s,%s,%s,%s,%s" + "&apiKey=7f7e3fa22e264910a6bb6f915107598b";
	
	private static final String URL_RECIPE_ID_TEMPLATE_SINGLE = "https://api.spoonacular.com/recipes/informationBulk"
			+ "?ids=%s" + "&apiKey=7f7e3fa22e264910a6bb6f915107598b";
	
	public List<Item> searchSingle(int id1) {
		// swap out %s in the url template with input params
		String url = String.format(URL_RECIPE_ID_TEMPLATE_SINGLE, id1);

		CloseableHttpClient httpclient = HttpClients.createDefault();

		// Create a custom response class
		ResponseHandler<List<Item>> responseHandler = new ResponseHandler<List<Item>>() {
			@Override
			public List<Item> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status != 200) {
					return new ArrayList<>();
				}
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					return new ArrayList<>();
				}
				String responseBody = EntityUtils.toString(entity);
				JSONArray array = new JSONArray(responseBody);

				return getItemList(array);
			}
		};

		try {
			return httpclient.execute(new HttpGet(url), responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}
	
	public List<Item> searchFew(int id1, int id2, int id3, int id4, int id5) {
		// swap out %s in the url template with input params
		String url = String.format(URL_RECIPE_ID_TEMPLATE_FEW, id1, id2, id3, id4, id5);

		CloseableHttpClient httpclient = HttpClients.createDefault();

		// Create a custom response class
		ResponseHandler<List<Item>> responseHandler = new ResponseHandler<List<Item>>() {
			@Override
			public List<Item> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status != 200) {
					return new ArrayList<>();
				}
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					return new ArrayList<>();
				}
				String responseBody = EntityUtils.toString(entity);
				JSONArray array = new JSONArray(responseBody);

				return getItemList(array);
			}
		};

		try {
			return httpclient.execute(new HttpGet(url), responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	public List<Item> search(int id1, int id2, int id3, int id4, int id5, int id6, int id7, int id8, int id9, int id10, int id11) {
		// swap out %s in the url template with input params
		String url = String.format(URL_RECIPE_ID_TEMPLATE_MANY, id1, id2, id3, id4, id5, id6, id7, id8, id9, id10, id11);

		CloseableHttpClient httpclient = HttpClients.createDefault();

		// Create a custom response class
		ResponseHandler<List<Item>> responseHandler = new ResponseHandler<List<Item>>() {
			@Override
			public List<Item> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status != 200) {
					return new ArrayList<>();
				}
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					return new ArrayList<>();
				}
				String responseBody = EntityUtils.toString(entity);
				JSONArray array = new JSONArray(responseBody);

				return getItemList(array);
			}
		};

		try {
			return httpclient.execute(new HttpGet(url), responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	private List<Item> getItemList(JSONArray array) {
		List<Item> itemList = new ArrayList<>();

		// get each recipe item
		for (int i = 0; i < array.length(); i++) {
			JSONObject recipeObject = array.getJSONObject(i);

			// get amount, unit and ingredient in a recipe
			JSONArray ingredientsArray = recipeObject.getJSONArray("extendedIngredients");
			List<Double> amountsList = new ArrayList<>();
			List<String> unitsList = new ArrayList<>();
			List<String> ingredientsList = new ArrayList<>();
			for (int j = 0; j < ingredientsArray.length(); j++) {
				JSONObject ingredientObject = ingredientsArray.getJSONObject(j);
				JSONObject measureObject = ingredientObject.getJSONObject("measures").getJSONObject("us");
				amountsList.add(getDoubleFieldOrEmpty(measureObject, "amount"));
				unitsList.add(getStringFieldOrEmpty(measureObject, "unitShort"));
				ingredientsList.add(getStringFieldOrEmpty(ingredientObject, "name"));
			}

			// get instructions in a recipe
			List<String> instrcutionsList = new ArrayList<>();
			if (recipeObject.getJSONArray("analyzedInstructions").length() != 0) {
				JSONArray instrcutionsArray = recipeObject.getJSONArray("analyzedInstructions").getJSONObject(0)
						.getJSONArray("steps");
				for (int k = 0; k < instrcutionsArray.length(); k++) {
					JSONObject instructionObject = instrcutionsArray.getJSONObject(k);
					instrcutionsList.add(getStringFieldOrEmpty(instructionObject, "step"));
				}
			}

			Item item = Item.builder().itemId(getIntFieldOrEmpty(recipeObject, "id"))
					.imageUrl(getStringFieldOrEmpty(recipeObject, "image"))
					.title(getStringFieldOrEmpty(recipeObject, "title")).amounts(amountsList).units(unitsList)
					.ingredients(ingredientsList).instructions(instrcutionsList)
					.sourceUrl(getStringFieldOrEmpty(recipeObject,"sourceUrl"))
					.build();

			// item -> itemList
			itemList.add(item);
		}
		return itemList;
	}

	private String getStringFieldOrEmpty(JSONObject obj, String field) {
		return obj.isNull(field) ? "" : obj.getString(field);
	}

	private int getIntFieldOrEmpty(JSONObject obj, String field) {
		return obj.isNull(field) ? 0 : obj.getInt(field);
	}

	private double getDoubleFieldOrEmpty(JSONObject obj, String field) {
		return obj.isNull(field) ? 0 : obj.getDouble(field);
	}

}
