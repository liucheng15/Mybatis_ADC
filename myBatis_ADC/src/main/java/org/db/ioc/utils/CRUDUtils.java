package org.db.ioc.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class CRUDUtils extends DBUtils {
	private static Connection con = null;
	private static PreparedStatement stmt = null;

	
	public boolean CUDUtils(String sql, Object[] objects) {
		boolean b = false;
		con = getConnection();
		try {
			stmt = con.prepareStatement(sql);
			if (objects != null) {
				
				for (int i = 0; i < objects.length; i++) {
					
					stmt.setObject(i + 1, objects[i]);
				}
			}
			int rows = stmt.executeUpdate();
			if (rows > 0) {
				b = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, stmt, con);
		}

		return b;

	}
	
	public ResultSet QUERYUtils(String sql, Object[] objects) {
		ResultSet rs = null;
		con = getConnection();
		try {
			stmt = con.prepareStatement(sql);
			if (objects != null) {
				
				for (int i = 0; i < objects.length; i++) {
					
					stmt.setObject(i + 1, objects[i]);
				}
			}
			rs = stmt.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public void closeResultSet(ResultSet rs) {
		close(rs, stmt, con);
	}

	
}
