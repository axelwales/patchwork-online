package com.patchwork.db;
import java.util.*;
import java.sql.*;
public class StatementProcessor
{
	public static String buildInsertStatement(String table, String[] attributes) {
		String result = "INSERT INTO " + table;
		String attributeString = "(";
		String valueString = "VALUES(";
		for(int i = 0; i<attributes.length; i++) {
			attributeString += attributes[i];
			valueString += "?";
			if( i!=attributes.length-1 ) {
				attributeString += ", ";
				valueString += ",";
			} else {
				attributeString += ") ";
				valueString += ")";
			}
		}
		return result + attributeString + valueString;
	}
	
	public static String buildUpdateStatement(String table, String[] attributes) {
		String result = "Update " + table + " SET ";
		String attributeString = "";
		String conditionString = "WHERE id=?";
		for(int i = 0; i<attributes.length; i++) {
			attributeString += attributes[i] + "=?";
			if( i!=attributes.length-1 ) {
				attributeString += ", ";
			} else {
				attributeString += " ";
			}
		}
		return result + attributeString + conditionString;
	}

	public static void executePreparedInsertStatement(String table, LinkedHashMap<String,Object> values, Connection con) throws SQLException {
		Set<String> keySet = values.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		String query = buildInsertStatement(table, keyArray);
		executePreparedInsertOrUpdate(query,values.values(),con);
	}
	
	public static void executePreparedUpdateStatement(String table, LinkedHashMap<String,Object> values, Long id, Connection con) throws SQLException {
		Set<String> keySet = values.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		String query = buildUpdateStatement(table, keyArray);
		values.put("whereCondition", id);
		executePreparedInsertOrUpdate(query,values.values(),con);
	}
	
	public static void executePreparedInsertOrUpdate(String query, Collection<Object> values, Connection con) throws SQLException {
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			int i = 0;
			for( Object value : values) {
				stmt.setObject(i+1, value);
				i++;
			}
			stmt.executeUpdate();
		}
	}
	
	public static String buildSelectStatement(String table, String[] attributes, String[] conditions) {
		String attributeString = "SELECT ";
		String tableString = "FROM " + table;
		String conditionString = " WHERE ";
		if( attributes == null)
			attributeString += "* ";
		else {
			for(int i = 0; i<attributes.length; i++) {
				attributeString += attributes[i];
				if( i!=attributes.length-1 )
					attributeString += ", ";
				else {
					attributeString += " ";
				}
			}
		}
		if(conditions == null)
			conditionString = "";
		else {
			for(int i = 0; i<conditions.length; i++) {
				conditionString += conditions[i];
				if( i!=attributes.length-1 )
					attributeString += " ";
			}
		}
		return attributeString + tableString + conditionString;
	}
	
	public static void execute(String query, Connection con) throws SQLException {		
		try (Statement stmt = con.createStatement()) {
			stmt.execute(query);
		}
	}
	
	public static void executeBatch(String[] querys, Connection con) throws SQLException {		
		for( String query : querys ) {
			execute(query,con);
		}
	}
	
	public static void executePatchesInit(Connection con) {
		String query = "INSERT INTO Patches VALUES(?,?)";
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			for( int i = 0; i<35; i++) {
				stmt.setObject(1, i);
				stmt.setObject(2, i);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {}
	}
	public static void executeInit(String[] initStatements, Connection con) {
		for( String query : initStatements ) {
			try {
				execute(query,con);
			} catch (SQLException e) {
			}
		}		
	}

	public static List<String[]> executeQuery(String query, Connection con) {		
		List<String[]> result = null;
		try (Statement stmt = con.createStatement()) {
			ResultSet r = stmt.executeQuery(query);
			result = TableBuilder.buildTable(r);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}

class TableBuilder {
	
	static LinkedList<String[]> buildTable(ResultSet r) throws SQLException {
		LinkedList<String[]> tableRows = new LinkedList<String[]>();
		ResultSetMetaData rmd = r.getMetaData();
		int columnCount = rmd.getColumnCount();
		String[] columnNames = new String[columnCount];
		for (int i = 1; i<=columnCount; i++) {
			columnNames[i-1] = rmd.getColumnName(i);
		}
		tableRows.add(columnNames);
		while (r.next()) {
			String[] row = new String[columnCount];
			for (int i = 1; i<=columnCount; i++) {
				row[i-1] = r.getString(i);
			}
			tableRows.add(row);
		}
		return tableRows;
	}
}