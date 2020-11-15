//package recommendation;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import javax.xml.bind.JAXBException;
//
//import org.dmg.pmml.FieldName;
//import org.jpmml.evaluator.Evaluator;
//import org.jpmml.evaluator.FieldValue;
//import org.jpmml.evaluator.InputField;
//import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
//import org.xml.sax.SAXException;
//
//import db.MySQLConnection;
//
//public class Model {
//	static private Evaluator evaluator;
//	static private MySQLConnection connection;
//	static List<String> freqWords;
//	static List<? extends InputField> inputFields;
//	static Map<FieldName, FieldValue> arguments;
//	
//	public static void loadModel() {
//		System.out.println("loading model...");
//		try {
//			System.out.println("Working Directory = " + System.getProperty("user.dir"));
//			evaluator = new LoadingModelEvaluatorBuilder().load(new File("svm_test.pmml")).build();
//			// Building a model/ evaluator from a PMML file
//			System.out.println("done load svm model");
//		} catch (IOException | SAXException | JAXBException e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public static void loadFreqWords() {
//		// get top N frequent ingredients
//		connection = new MySQLConnection();
//		HashMap<String, Integer> map = connection.getIngredientFreq();
//		freqWords = getFreqWords(map, 1000);
//		System.out.println("done get freWords");
//		connection.close();
//	}	
//	
//	public static List<Integer> runModel(List<String> userFridge, int N) {
//			// transform userFridge for model
//			List<Double> hasIngre = getHasIngreList(userFridge, freqWords);
//			System.out.println("done load user fridge");
//			
//			// prepare argument for model
//			inputFields = evaluator.getInputFields();
////			System.out.println("Input fields: " + inputFields);
//			arguments = setUpArguments(inputFields, hasIngre);
//			System.out.println("done set up arguments");
//
//			// Evaluating the model with known-good arguments
//			Map<FieldName, ?> results = evaluator.evaluate(arguments);
//			System.out.println("done evaluate model");
//			
//			// get top N matched itemIds
//			List<Integer> topNMatchItemId = getTopNMatch(results, N);
//			System.out.println("done find top N matches");
//			return topNMatchItemId;
//	}
//	
//	public static List<Integer> getTopNMatch(Map<FieldName, ?> results, int N) {
//		String str = results.toString();
//		str = str.substring(str.indexOf("[") + 1);
//		str = str.substring(0, str.indexOf("]"));
//		String[] parts = str.split(", ");
//		HashMap<Integer, Double> mapProb = new HashMap<>();
//		for (String s : parts ) {
//			String[] parts2 = s.split("=");
//			mapProb.put(Integer.parseInt(parts2[0]), Double.parseDouble(parts2[1]));
//			}
//		
//		List<Integer> topNMatch = mapProb.entrySet().stream()
//				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(N)
//				.map(Map.Entry::getKey)
//				.collect(Collectors.toList());
//		return topNMatch;
//	}
//	
//	public static Map<FieldName, FieldValue> setUpArguments(List<? extends InputField> inputFields, List<Double> hasIngre) {
//		Map<String, Double> inputRecord = new HashMap<>();
//		for (int i = 0; i < inputFields.size(); i++) {
//			inputRecord.put(inputFields.get(i).getFieldName().toString(), hasIngre.get(i));
//		}
//
//		Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
//
//		// Mapping the record field-by-field from data source schema to PMML schema
//		for (InputField inputField : inputFields) {
//			FieldName inputName = inputField.getName();
//			Object rawValue = inputRecord.get(inputName.getValue());
//
//			// Transforming an arbitrary user-supplied value to a known-good PMML value
//			FieldValue inputValue = inputField.prepare(rawValue);
//			arguments.put(inputName, inputValue);
//		}
//		return arguments;
//	}
//	
//	public static List<Double> getHasIngreList (List<String> userFridge, List<String> freqWords) {
//		List<Double> hasIngre = new ArrayList<>();
////			for (String fw: freqWords) {
////				if (userFridge.contains(fw)) {
////					hasIngre.add(1.0);
////				} else {
////					hasIngre.add(0.0);
////				}
////			}
//			int hasFound = 0;
//			for (String fw : freqWords) {
//				hasFound = 0;
//				for (int i = 0; i < userFridge.size(); i++) {
//					if (userFridge.get(i).contains(fw) || fw.contains(userFridge.get(i))) {
//						hasIngre.add(1.0);
//						hasFound = 1;
//						break;
//					}
//				}
//				if (hasFound == 0) {
//					hasIngre.add(0.0);
//				}
//			}
//		return hasIngre;
//	}
//
////	public static int calculateTopN(HashMap<String, Integer> map) {
////		int total = map.keySet().size();
////		int topN = total * 9 / 10;
//////		System.out.println("total ingredients: " + total + "\n");
//////		System.out.println("top n = :" + topN + "\n");
////		return topN;
////	}
//
//	// helper method to get top N most frequent ingredients
//	public static List<String> getFreqWords(HashMap<String, Integer> map, int topN) {
//		// sort the HashMap by frequency and get top N frequent key-value pair
//		List<String> freqWords = map.entrySet().stream()
//				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(topN)
//				.map(Map.Entry::getKey)
//				.collect(Collectors.toList());
//		
//		return freqWords;
//	}
//}