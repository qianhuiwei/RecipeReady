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

/**
 * Servlet implementation class Fridge.
 */
public class Fridge extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Fridge() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// only allow accessing fridge when a login session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}

		// read fridge table
		String userId = request.getParameter("user_id");

		MySQLConnection connection = new MySQLConnection();
		List<String> ingredients = connection.getFridge(userId);
		connection.close();

		JSONArray array = new JSONArray();
		for (String ingredient : ingredients) {
			array.put(ingredient);
		}

		RpcHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// only allow modifying fridge when a login session exists
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
			String ingredient = obj.getString("ingredient");

			connection.setFridge(userId, ingredient);

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
		// only allow modifying fridge when a login session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}

		MySQLConnection connection = new MySQLConnection();
		try {
			JSONObject obj = new JSONObject(IOUtils.toString(request.getReader()));
			String userId = obj.getString("user_id");
			if (obj.has("ingredient")) {
				String ingredient = obj.getString("ingredient");
				connection.unsetFridge(userId, ingredient);
			} else {
				connection.clearFridge(userId);
			}

			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS")); // FOR TESTING
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
