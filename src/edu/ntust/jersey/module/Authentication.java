package edu.ntust.jersey.module;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import edu.ntust.dao.MySQL;
import edu.ntust.notary.Hash;

@Path("auth")
public class Authentication{
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	
	public String authentication(@FormParam("username") String userNAME, 
								@FormParam("password") String passWORD, 
								@FormParam("app_id") String app_ID) throws Exception{
		PreparedStatement query = null;
		Connection conn = null;
		String returnString = null;
		
		try { 
			conn = MySQL.getInstance().getConnection();
			query = conn.prepareStatement("SELECT * FROM member WHERE username = ? AND password = ?");
			query.setString(1, userNAME);
			query.setString(2, Hash.sha1(passWORD));
			ResultSet rs = query.executeQuery();
			while (rs.next()){
				JSONObject jobject = new JSONObject();
				String username = rs.getString(1);
				String password = rs.getString(2);
				
				System.out.println("1" + username);
				System.out.println("2" + password);
				System.out.println("3" + userNAME);
				System.out.println("4" + Hash.sha1(passWORD));
				
				int kk = username.compareTo(userNAME);
				int gg = password.compareTo(Hash.sha1(passWORD));
				
				System.out.println(kk);
				System.out.println(gg);
				
				if (kk == 0 && gg == 0){  
					jobject.put("login", "pass");
					jobject.put("key", Hash.sha1(app_ID));
				}
				else {
					jobject.put("login", "fail");
				}
				
				returnString = jobject.toString();
			}
			query.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		finally{
			if (conn != null)
				conn.close();
		}	
		return returnString;
	}
}