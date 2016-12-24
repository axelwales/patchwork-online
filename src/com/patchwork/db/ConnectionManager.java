package com.patchwork.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;


public class ConnectionManager {
	
	private static ConnectionManager cm = null;
	
	DataSource ds = null;
	Connection conn = null;
	
	private ConnectionManager() {
		try {
		// Obtain our environment naming context
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
	
		// Look up our data source
		ds = (DataSource)envCtx.lookup("jdbc/patchworkDB");		
		
		} catch (NamingException e) {e.printStackTrace();}
	}
	
	public static void start() {
		if( cm == null )
			cm = new ConnectionManager();
	}
	
	public static Connection getConnection() {
		if( cm == null )
			cm = new ConnectionManager();
		try {
			// Allocate and use a connection from the pool
			cm.conn = cm.ds.getConnection();
		} catch(SQLException e) {}
		return cm.conn;
	}

	public static void close() {
		try {
			if( cm != null ) {
				if( cm.ds != null ) {
					if(cm.conn != null)
						cm.conn.close();
					cm.ds.close();
				}
				cm = null;
			}
		} catch (SQLException e) {}
	}
}
