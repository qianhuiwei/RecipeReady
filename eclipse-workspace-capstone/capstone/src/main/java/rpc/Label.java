package rpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;

import db.MySQLConnection;
import external.ItemRecognition;

/**
 * Servlet implementation class Label
 */
public class Label extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Label() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// only allow accessing label when a login session exists
				HttpSession session = request.getSession(false);
				if (session == null) {
					response.setStatus(403);
					return;
				}

				
				String imageURL = request.getParameter("image_url");
//				List<String> labels = ItemRecognition.getLables(imageURL);
//				
//				JSONArray array = new JSONArray();
//				for (String ingredient : labels) {
//					array.put(ingredient);
//				}
//
//				RpcHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}