package com.patchwork.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdManager {
	private static final String idQuery = "select id_seq.NEXTVAL from dual";
	public static long getNextId(Connection con) {
		long id = -1;
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(idQuery);
			ResultSet r = stmt.executeQuery();
			if(r.next())
			     id = r.getLong(1);
			stmt.close();
		} catch (SQLException e) {
			return -1;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {}
		}
		return id;
	}
}
