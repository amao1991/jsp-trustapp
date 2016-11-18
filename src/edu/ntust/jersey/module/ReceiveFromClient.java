package edu.ntust.jersey.module;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import edu.ntust.dao.MySQL;

@Path("receive_client_data")
public class ReceiveFromClient {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String ClientData( 
			@FormParam("token") String token, 
			@FormParam("mac_address") String mac_address) throws Exception{
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = MySQL.getInstance().getConnection();  // get connection object
			stmt = conn.createStatement();  //create statement object
			//stmt.executeUpdate("DROP TABLE IF EXISTS client_data;");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS client_data (token text COLLATE utf8mb4_unicode_ci NOT NULL,mac_address varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");
			stmt.executeUpdate("INSERT INTO client_data (token, mac_address) VALUES ('" + token.toString() + "','" + mac_address.toString() + "');");
			stmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			if (conn != null)
				conn.close();
		}
		
		return null;
	}
}