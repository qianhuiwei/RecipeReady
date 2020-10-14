package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import db.MySQLConnection;
import entity.Item;
import recommendation.Model;

/**
 * Servlet implementation class RecommendItem
 */
public class RecommendItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static List<String> userFridgeCache;
	private static Set<Item> itemListCache;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecommendItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// only allow accessing history when a login session exists
				HttpSession session = request.getSession(false);
				if (session == null) {
					response.setStatus(403);
					return;
				}

				// read history table + item table
				String userId = request.getParameter("user_id");

				// get userFride
				MySQLConnection connection = new MySQLConnection();
				List<String> userFridge = connection.getFridge(userId);
				System.out.println("userfridge" + userFridge);
				
				List<Integer> recommendedItemIds = connection.getTopNMatchIds(userFridge, 5);
				System.out.print("recommend ids size: "+ recommendedItemIds.size());
				List<Item> itemList = connection.getRecommendItems(recommendedItemIds);
				JSONArray array = new JSONArray();
				for (Item item : itemList) {
					JSONObject obj = item.toJSONObject();
					array.put(obj);
				}
				RpcHelper.writeJsonArray(response, array);
				
//				if (!userFridge.equals(userFridgeCache)) {
//					// get recommend ids from model
////					Model.loadModel();
////					Model.loadFreqWords();
//					List<Integer> recommendedItemIds = Model.runModel(userFridge, 10);
//					System.out.println("recommend list size: " + recommendedItemIds.size());
//					
//					// get item from db
//					Set<Item> itemList = connection.getRecommendItems(recommendedItemIds);
//					connection.close();
//					itemListCache = itemList;
//					userFridgeCache = userFridge;
//					
//					JSONArray array = new JSONArray();
//					for (Item item : itemList) {
//						JSONObject obj = item.toJSONObject();
//						array.put(obj);
//					}
//
//					RpcHelper.writeJsonArray(response, array);
//				} else {
//					JSONArray array = new JSONArray();
//					for (Item item : itemListCache) {
//						JSONObject obj = item.toJSONObject();
//						array.put(obj);
//					}
//
//					RpcHelper.writeJsonArray(response, array);
//				}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
