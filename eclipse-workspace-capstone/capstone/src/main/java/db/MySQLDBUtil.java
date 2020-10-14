package db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MySQLDBUtil {
	private static final String INSTANCE = "capstone.c7mh8y6bp4gb.us-east-2.rds.amazonaws.com";
	private static final String PORT_NUM = "3306";
	public static final String DB_NAME = "capstone";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "12345678";

	public static final String URL = "jdbc:mysql://"
			+ INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD 
			+ "&autoReconnect=true&serverTimezone=UTC";
	
	
//	public static String concateDoubles(List<Double> list) {
//		String output = "";
//		if (list.size() == 0) {
//			return output;
//		}
//		for (Double d: list) {
//			output += Double.toString(d);
//			output += "\n";
//		}
//		return output;
//	}
	
	
	public static String concateStrings(List<String> list) {
		String output = "";
		if (list.size() == 0) {
			return output;
		}
		for (String s: list) {
			output += s;
			output += "\n";
		}
		return output;
	}
	
//	public static List<Double> textToDoubles(String text) {
//		if (text == null || text == "") {
//			return new ArrayList<Double>();
//		}
//		
//		List<Double> output = new ArrayList<>();
//        String[] array = text.split("\n"); 
//  
//        for (String d : array)  {
//            output.add(Double.parseDouble(d));
//        }
//        return output;
//	}
	
	public static List<String> textToStrings(String text) {
		if (text == null || text == "") {
			return new ArrayList<String>();
		}
		
        String[] array = text.split("\n"); 
  
        return Arrays.asList(array);
	}

}