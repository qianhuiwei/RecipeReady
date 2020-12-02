package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import db.MySQLConnection;
import entity.Item;
import external.RecipeAPIClient;

public class TestItemsTable {

	@Test
	public void test() {
		RecipeAPIClient client = new RecipeAPIClient();
		int itemId = 100000;
		List<Item> itemList = client.searchSingle(itemId);

		MySQLConnection connection = new MySQLConnection();
		connection.testMySQLConnection();

		// test saveItem method
		Item itemInserted = itemList.get(0);
		connection.saveItem(itemInserted);

		Item itemRetreived = connection.getItems().get(0);

		// checked with getItems() method
		assertEquals(itemInserted.getItemId(), itemRetreived.getItemId());
		assertEquals(itemInserted.getImageUrl(), itemRetreived.getImageUrl());
		assertEquals(itemInserted.getTitle(), itemRetreived.getTitle());
		assertEquals(itemInserted.getInstructions().get(0), itemRetreived.getInstructions().get(0));
		assertEquals(itemInserted.getSourceUrl(), itemRetreived.getSourceUrl());

		// checked with getItem(int id) method
		Item itemRetreived2 = connection.getItem(itemId);
		assertEquals(itemInserted.getItemId(), itemRetreived2.getItemId());
		assertEquals(itemInserted.getImageUrl(), itemRetreived2.getImageUrl());
		assertEquals(itemInserted.getTitle(), itemRetreived2.getTitle());
		assertEquals(itemInserted.getInstructions().get(0), itemRetreived2.getInstructions().get(0));
		assertEquals(itemInserted.getSourceUrl(), itemRetreived2.getSourceUrl());

		System.out.println("saveItem() db operation checked");

		connection.close();
	}
}
