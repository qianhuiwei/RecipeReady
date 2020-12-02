package rpc;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.MySQLConnection;
import entity.Item;

/**
 * Servlet implementation class ItemHistory
 */
public class ItemHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ItemHistory() {
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

		MySQLConnection connection = new MySQLConnection();
		List<Item> items = connection.getFavoriteItems(userId);
		connection.close();

		JSONArray array = new JSONArray();
		for (Item item : items) {
			JSONObject obj = item.toJSONObject();
			try {
				obj.put("favorite", true); // for frontend to mark item as favorite
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(obj);
		}

		RpcHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// only allow modifying history when a login session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}

		MySQLConnection connection = new MySQLConnection();

		try {
			// get request body, convert it to string, and then convert it to JSONObject
			JSONObject obj = new JSONObject(IOUtils.toString(request.getReader()));
			String userId = obj.getString("user_id");
			Item item = RpcHelper.parseFavoriteItem(obj.getJSONObject("favorite"));

			connection.setFavoriteItems(userId, item);

			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS")); // FOR TESTING
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// only allow modifying history when a login session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}

		MySQLConnection connection = new MySQLConnection();
		try {
			JSONObject obj = new JSONObject(IOUtils.toString(request.getReader()));
			String userId = obj.getString("user_id");
			Item item = RpcHelper.parseFavoriteItem(obj.getJSONObject("favorite"));
			connection.unsetFavoriteItems(userId, item.getItemId());

			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS")); // FOR TESTING
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
