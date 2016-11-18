package edu.ntust.jersey.module;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import edu.ntust.dao.MySQL;

@Path("send_list")
public class RequestList{
	@Context
	ServletContext context;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadFile(@Context HttpServletRequest request) throws SQLException, JSONException{
		//String location = context.getRealPath("/");
		Connection conn = null;
		Statement query = null;
		String responseText = null;
		JSONObject list = new JSONObject();

		try {
			conn = MySQL.getInstance().getConnection();
			query = conn.createStatement(); 
			
			//query.execute("SELECT nickname, mac_address FROM client_data;");
			//ResultSet rs = query.getResultSet();  // 取得查詢結果
			
			ResultSet rs = query.executeQuery("SELECT * FROM client_data WHERE nickname = 'test'");
			while (rs.next()){
				String nickname = rs.getString(3);
				String mac_address = rs.getString(2);
				list.put("nickname", nickname);
				list.put("mac_address", mac_address);
			}
			
			//query.execute("SELECT mac_address FROM client_data;");
			//ResultSet rs2 = query.getResultSet();  // 取得查詢結果
			//String rs4 = rs2.toString();
			
			//JSONArray returnlist = new JSONArray(Arrays.asList(rs));  // array to JSON
			//list = returnlist.toString();  // JSON to String
		}
		finally {
			if (conn != null)
				conn.close();
		}
		return list.toString();
	}
}