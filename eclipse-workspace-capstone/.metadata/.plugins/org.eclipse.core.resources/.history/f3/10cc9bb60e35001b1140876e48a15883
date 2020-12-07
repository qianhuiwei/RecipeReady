package external;

import java.util.List;
import org.json.JSONException;

import db.MySQLConnection;
import entity.Item;

//---------------------------------------------------------------------------------
// Run this method as Java Application to collect recipe data from the external API
// --------------------------------------------------------------------------------
public class CollectRecipe {
	
	static int id1 = 135000;

	public static void main(String[] args) {
		
		RecipeAPIClient client = new RecipeAPIClient();

		int listSize = 0;
		id1--;
		
		while (listSize != 150) { // the API allows no more than 150 requests per day
			id1++;
			List<Item> itemList = client.searchSingle(id1);
			System.out.println("id " + id1);
			System.out.println("query result " + itemList.size());
			
			if (itemList.size() == 0) {
				continue;
			} else {
				MySQLConnection connection = new MySQLConnection();
				try {
					for (Item item : itemList) {
						connection.saveItem(item);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					System.out.print(listSize + ": successfully saved item" + "\n");
					listSize++;
					connection.close();
				}
			}
		}
		System.out.print("successfully saved " + listSize + " items" + "\n");
	}
}
