package com.patchwork.db;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;
import java.util.regex.*;
public class SQLFileParser
{
	InputStream fileStream;
	List<String> lines;
	List<String> statements;
	
	SQLFileParser( InputStream fileStream ) {
		this.fileStream = fileStream;
	}
	
	public void readFile() {
		lines = new LinkedList<String>();
		BufferedReader inFile = new BufferedReader(new InputStreamReader(fileStream,
			          StandardCharsets.UTF_8));
		String line = null;
		try {
			while((line = inFile.readLine()) != null) {
			    lines.add(line);
			}
			inFile.close();
		} catch(IOException e) {e.printStackTrace();}
		
	}
	
	public void parseFile() {
		removeInLineComments();
		removeBlockComments();
		splitStatements();
	}
	
	public void removeInLineComments() {
		LinkedList<String> result = new LinkedList<String>();
		Pattern p = Pattern.compile("--.*");
		Matcher m;
		for ( String line : lines ) {
			m = p.matcher(line);
			line = m.replaceAll("");
			result.add(line);
		}
		lines = result;
	}
	
	public void removeBlockComments() {
		LinkedList<String> result = new LinkedList<String>();
		Pattern pStart = Pattern.compile("/\\*.*");
		Pattern pEnd = Pattern.compile(".*\\*/");
		Pattern pComplete = Pattern.compile("/\\*.*\\*/");
		boolean inBlock = false;
		Matcher m;
		for ( String line : lines) {
			if( inBlock )
				m = pEnd.matcher(line);
			else
				m = pStart.matcher(line);
			if (m.matches() && !inBlock) {
				m = pComplete.matcher(line);
				if (!m.matches()) {
					m = pStart.matcher(line);
					inBlock = true;
				}
			}
			else if (m.matches() && inBlock)
				inBlock = false;
			
			if (!m.matches() && !inBlock) {
				line = m.replaceAll("");
				result.add(line);
			}
		}
		lines = result;
	}
	
	public void splitStatements() {
		LinkedList<String> result = new LinkedList<String>();
		Pattern p = Pattern.compile(";");
		Matcher m;
		String[] splitStrings;
		String currentString = "";
		for ( String line : lines ) {
			m = p.matcher(line);
			if(m.find()) {
				splitStrings = p.split(line);
				for ( int i = 0; i < splitStrings.length; i++ ) {
					String s = splitStrings[i];
					currentString = currentString + s.trim();
					if(splitStrings.length == 1 || splitStrings.length != i + 1 ) {
						result.add(currentString);
						currentString = "";
					}
				}
			}
			else {
				currentString = currentString + line.trim();
			}
		}
		statements = result;
	}
	
	public List<String> getStatements() {
		return statements;
	}
	
	public static List<String> getStatements(InputStream fileStream) {
		SQLFileParser sfp = new SQLFileParser(fileStream);
		sfp.readFile();
		sfp.parseFile();
		return sfp.getStatements();
	}
}