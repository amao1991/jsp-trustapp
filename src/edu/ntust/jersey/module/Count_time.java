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

@Path("app")
public class Count_time {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String identification(@FormParam("username") String username,
								@FormParam("app_id") String app_id) throws Exception{
		Connection conn = null;
		PreparedStatement query = null;
		PreparedStatement stmt = null;
		String returnString = null;
		
		try {
			conn = MySQL.getInstance().getConnection();
			query = conn.prepareStatement("SELECT purchase.count, purchase.time_restrict_id, purchase.purchase_time, time_restrict_new.start_day, time_restrict_new.end_day " +
										"FROM purchase " +
										"INNER JOIN time_restrict_new ON purchase.time_restrict_id = time_restrict_new.time_restrict_id " +
										"WHERE purchase.username = ? AND purchase.app_id = ?");
			query.setString(1, username);
			query.setString(2, app_id);
			ResultSet rs = query.executeQuery();
			
			while (rs.next()) {
				JSONObject data = new JSONObject();
				
				// 次數判別
				System.out.println(rs.getInt("count"));
				
				if (rs.getInt("count") == 0) {
					data.put("count", false);
				}
				else {
					data.put("count", true);
					int counter = rs.getInt("count");
					counter--;
					System.out.println(counter);
					stmt = conn.prepareStatement("UPDATE purchase SET count = " + counter + " WHERE username = ?");
					query.setString(1, username);
				}
				

				// 時間判別，count != 0, 進入時間判別, time = 0, 時間後, time = 1, 時間前, time = 2, 時間區間
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				Calendar c3 = Calendar.getInstance();

				System.out.println(rs.getInt("time_restrict_id"));
				
				switch (rs.getInt("time_restrict_id")) {
					case 0:
						c1.setTime(rs.getDate("purchase_time"));
						c2.setTime(rs.getDate("start_day"));

						int result = c1.compareTo(c2);		
						if (result > 0) {
							data.put("time", true);
						}
						else {
							data.put("time", false);
						}
						break;
					case 1:
						c1.setTime(rs.getDate("purchase_time"));
						c2.setTime(rs.getDate("end_day"));

						int result1 = c1.compareTo(c2);
						if (result1 < 0){
							data.put("time", true);
						}
						else{
							data.put("time", false);
						}
						break;
					case 2:
						c1.setTime(rs.getDate("purchase_time"));
						c2.setTime(rs.getDate("start_day"));
						c3.setTime(rs.getDate("end_day"));

						int result2 = c1.compareTo(c2);
						int result3 = c1.compareTo(c3);
						if (result2 > 0 && result3 < 0){
							data.put("time", true);
						}
						else{
							data.put("time", false);
						}
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
