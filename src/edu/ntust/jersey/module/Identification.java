package edu.ntust.jersey.module;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.sql.PreparedStatement;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

import edu.ntust.dao.MySQL;

//硬體辨識
@Path("id")
public class Identification {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String identification(@FormParam("username") String username, @FormParam("id") String id) throws Exception{
		Connection conn = null;
		PreparedStatement query = null;
		String returnString = null;
		JSONObject data = new JSONObject();
		
		try {
			conn = MySQL.getInstance().getConnection();
			query = conn.prepareStatement("SELECT identification FROM member WHERE username = ? ");
			query.setString(1, username);
			ResultSet rs = query.executeQuery();
			
			while (rs.next()) {
				System.out.println(rs.getString("identification"));
				
				int kk = id.compareTo(rs.getString("identification"));
				
				System.out.println(kk);
				
				if (kk == 0){
					data.put("id", true);
				}
				else {
					data.put("id", false);
				}
					
				System.out.println(data);
				
				returnString = data.toString();
			}		
		}
		finally {
			if (conn != null)
				conn.close();
		}
		return returnString;
	}
}
