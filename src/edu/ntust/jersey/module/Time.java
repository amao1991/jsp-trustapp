package edu.ntust.jersey.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import edu.ntust.dao.MySQL;
import edu.ntust.notary.Global;

@Path("time")
public class Time {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String identification(@FormParam("username") String username) throws Exception{
		Connection conn = null;
		PreparedStatement query = null;
		String returnString = null;
		
		try {
			conn = MySQL.getInstance().getConnection();
			query = conn.prepareStatement("SELECT purchase.purchase_time, time_restrict_new.start_day, time_restrict_new.end_day " +
										"FROM purchase " +
										"INNER JOIN time_restrict_new ON purchase.time_restrict_id = time_restrict_new.time_restrict_id " +
										"WHERE purchase.username = ? ");
			query.setString(1, username);
			ResultSet rs = query.executeQuery();
			
			while (rs.next()) {
				JSONObject data = new JSONObject();

				// 時間判別, 
				// time = 0, 時間後, 
				// time = 1, 時間前, 
				// time = 2, 時間區間
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				Calendar c3 = Calendar.getInstance();
				
				int time = Integer.parseInt(Global.time_restrict_id);
				
				switch (time) {
					case 0:
						c1.setTime(rs.getDate("purchase_time"));
						c2.setTime(rs.getDate("start_day"));

						int result = c1.compareTo(c2);		
						if (result > 0) {
							data.put("bool", true);
							data.put("time", Integer.parseInt(Global.time_restrict_id));
						}
						else {
							data.put("bool", false);
							data.put("time", Integer.parseInt(Global.time_restrict_id));
						}
						break;
					case 1:
						c1.setTime(rs.getDate("purchase_time"));
						c2.setTime(rs.getDate("end_day"));

						int result1 = c1.compareTo(c2);
						if (result1 < 0){
							data.put("bool", true);
							data.put("time", Integer.parseInt(Global.time_restrict_id));
						}
						else{
							data.put("bool", false);
							data.put("time", Integer.parseInt(Global.time_restrict_id));
						}
						break;
					case 2:
						c1.setTime(rs.getDate("purchase_time"));
						c2.setTime(rs.getDate("start_day"));
						c3.setTime(rs.getDate("end_day"));

						int result2 = c1.compareTo(c2);
						int result3 = c1.compareTo(c3);
						if (result2 > 0 && result3 < 0){
							data.put("bool", true);
							data.put("time", Integer.parseInt(Global.time_restrict_id));
						}
						else{
							data.put("bool", false);
							data.put("time", Integer.parseInt(Global.time_restrict_id));
						}
						break;
					case 3:
						data.put("bool", true);
						data.put("time", Integer.parseInt(Global.time_restrict_id));
						break;
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
