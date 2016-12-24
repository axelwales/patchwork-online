package com.patchwork.db;

import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import javax.servlet.ServletContext;
public class InitializeTables
{
	static String InitSchemaFile = "/WEB-INF/sql/init_schema.sql";
	
	public static void init(ServletContext ctx) {
		try (InputStream stream = ctx.getResourceAsStream(InitSchemaFile)) {
		List<String> initStatementsList = SQLFileParser.getStatements(stream);
		String[] initStatements = initStatementsList.toArray(new String[initStatementsList.size()]);
			try (Connection con = ConnectionManager.getConnection()) {
				StatementProcessor.executeInit(initStatements, con);
				StatementProcessor.executePatchesInit(con);
			} 
		} catch (IOException e) { e.printStackTrace(); 
		} catch (SQLSyntaxErrorException e) {
		} catch (SQLException e) { e.printStackTrace();
		}
	}
}