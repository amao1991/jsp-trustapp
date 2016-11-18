package edu.ntust.jersey.module;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.ntust.dao.MySQL;

public class GetTestRegID {
	
	public static String getRegID (String receive_mac_address) throws SQLException {
		Connection conn = null;
		Statement query = null;
		String RegID = null;

		try {
			conn = MySQL.getInstance().getConnection();
			query = conn.createStatement();
			ResultSet rs = query.executeQuery("SELECT token FROM client_data WHERE mac_address ='" + receive_mac_address + "'");
			RegID = rs.getString(1);		
		}
		finally {
			if (conn != null)
				conn.close();
		}
		return RegID;
	}
}
