package rpc;

import java.io.IOException;
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
import external.RecipeAPIClient;

/**
 * Servlet implementation class SearchItem
 */
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		// only allow searching when a login session exists
//		HttpSession session = request.getSession(false);
//		if (session == null) {
//			response.setStatus(403);
//			return;
//		}
//
//		int id1 = Integer.parseInt(request.getParameter("id1"));
//		int id2 = Integer.parseInt(request.getParameter("id2"));
//		String userId = request.getParameter("user_id");
//
//		MySQLConnection connection = new MySQLConnection();
//		Set<Integer> favoriteItemIds = connection.getFavoriteItemIds(userId);
//		connection.close();
//
//		RecipeAPIClient client = new RecipeAPIClient();
//		List<Item> itemList = client.search(id1, id2);
//
//		JSONArray array = new JSONArray();
//		for (Item item : itemList) {
//			JSONObject obj = item.toJSONObject();
//			obj.put("favorite", favoriteItemIds.contains(item.getItemId()));
//			array.put(obj);
//		}
//
//		RpcHelper.writeJsonArray(response, array);
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
