package recommendation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import db.MySQLConnection;

public class Export {
	public static void main(String[] args) {
		try (PrintWriter writer = new PrintWriter(new File("test.csv"))) {

			// write column names
			MySQLConnection connection = new MySQLConnection();
			HashMap<String, Integer> map = connection.getIngredientFreq();
			List<String> freqWords = Model.getFreqWords(map, 1000);
			System.out.println(freqWords);
			System.out.println(freqWords.size());

			StringBuilder sb = new StringBuilder();
			sb.append("item_id");
			sb.append(',');
			for (String s : freqWords) {
				sb.append(s);
				sb.append(',');
			}
			sb.append('\n');

			// insert rows
			HashMap<Integer, List<String>> id_ingred_map = connection.getItemIdIngred();
			Set<Integer> ids = id_ingred_map.keySet();

			for (int id : ids) {
				sb.append(id);
				sb.append(',');
				int hasFound = 0;

				for (String fw : freqWords) {
					hasFound = 0;
					List<String> currentItem = id_ingred_map.get(id);
					for (int i = 0; i < currentItem.size(); i++) {
						if (currentItem.get(i).contains(fw) || fw.contains(currentItem.get(i))) {
							sb.append(1);
							sb.append(',');
							hasFound = 1;
							break;
						}
					}
					if (hasFound == 0) {
						sb.append(0);
						sb.append(',');
					}
				}
				sb.append('\n');
			}

			writer.write(sb.toString());
			System.out.println("done!");

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

}