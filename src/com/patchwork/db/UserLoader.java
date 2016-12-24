package com.patchwork.db;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.patchwork.db.security.PasswordDigester;

public class UserLoader {
	
	public static boolean insertUser(String username, String password) {
		try {
			PasswordDigester.digest(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		if(PasswordDigester.securePassword == null)
			return false;
		
		String securePassword = PasswordDigester.saltString + "$1$" + PasswordDigester.securePassword;
		String insertUser = StatementProcessor.buildInsertStatement("Users", new String[]{"id","username","password"});
		String insertRole = StatementProcessor.buildInsertStatement("User_Roles", new String[]{"username","role"});
		try(Connection con = ConnectionManager.getConnection()) {
			long userid = IdManager.getNextId(con);
			if(userid == -1) {
				con.close();
				return false;
			}
			try(PreparedStatement stmt = con.prepareStatement(insertUser)) {
				stmt.setLong(1, userid);
				stmt.setString(2, username);
				stmt.setString(3, securePassword);
				stmt.executeUpdate();
			}
			try(PreparedStatement stmt = con.prepareStatement(insertRole)) {
				stmt.setString(1, username);
				stmt.setString(2, "user");
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean checkUserExists(String username) {
		String query = StatementProcessor.buildSelectStatement("Users", new String[]{"username"}, new String[]{"username = ?"});
		try(Connection con = ConnectionManager.getConnection()) {
			try(PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, username);
				try(ResultSet r = stmt.executeQuery()) {
					if (!r.isBeforeFirst() ) {
						r.close();
						stmt.close();
						con.close();
					    return false; 
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
