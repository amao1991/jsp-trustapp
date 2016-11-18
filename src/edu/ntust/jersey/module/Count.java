package edu.ntust.jersey.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import edu.ntust.dao.MySQL;
import edu.ntust.notary.Global;

@Path("count")
public class Count {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String identification(@FormParam("username") String username,
								@FormParam("app_id") String app_id) throws Exception{
		Connection conn = null;
		PreparedStatement query = null;
		PreparedStatement update = null;
		Statement stmt = null;
		String returnString = null;
		
		try {
			conn = MySQL.getInstance().getConnection();
			query = conn.prepareStatement("SELECT count FROM purchase WHERE username = ? AND app_id = ?");
			query.setString(1, username);
			query.setString(2, app_id);
			ResultSet rs = query.executeQuery();
			
			while (rs.next()) {
				JSONObject data = new JSONObject();

				
				if (Integer.parseInt(Global.count_or_not) == 0) {
					data.put("count", -1);
				}
				else {
					// 次數判別
					System.out.println(rs.getInt("count"));
					int counter = rs.getInt("count");
					
					if (rs.getInt("count") == 0) {
						data.put("count", counter);
					}
					else {
						counter--;
						data.put("count", counter);
						System.out.println(counter);			
						conn = MySQL.getInstance().getConnection();
						stmt = conn.createStatement();
						//UPDATE `purchase` SET `count`= 2 WHERE `username` = "Alice"
						stmt.executeUpdate("UPDATE purchase SET count = '" + counter + "' WHERE username = '" + username.toString() + "';");
					}
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
