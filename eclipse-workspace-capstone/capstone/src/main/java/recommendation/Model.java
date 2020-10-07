package recommendation;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import db.MySQLConnection;

public class Model {
	public static void main(String[] args) {
		// get top N frequent ingredients
		HashMap<String, Integer> map = getIngredientFrqMap();
		Set<String> freqWords = freqWords(map, calculateTopN(map));
		for (String s : freqWords) {
			System.out.println(s);
		}

		// use svm to train model
		

	}

	private static HashMap<String, Integer> getIngredientFrqMap() {
		// get ingredient frequency map from db
		MySQLConnection connection = new MySQLConnection();
		HashMap<String, Integer> map = connection.getIngredientFreq();
		connection.close();
		return map;
	}
	
	private static int calculateTopN(HashMap<String, Integer> map) {
		int total = map.keySet().size();
		int topN = total * 9 / 10;
		System.out.println("total ingredients: " + total + "\n");
		System.out.println("top n = :" + topN + "\n");	
		return topN;
	}

	// helper method to get top N most frequent ingredients
	private static Set<String> freqWords(HashMap<String, Integer> map, int topN) {
		// sort the HashMap by frequency and get top N frequent key-value pair
		Map<String, Integer> sortedMap = map.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(topN)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		// get top N ingredient names
		Set<String> freqWords = sortedMap.keySet();
		return freqWords;
	}
}