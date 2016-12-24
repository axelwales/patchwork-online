package com.patchwork.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadWriteValidator {
	private HashMap<Long, AtomicInteger> gameMap;
	private HashMap<Long, AtomicInteger> threadMap;
	private HashMap<Long, Long> threadToGame;
	private static ReadWriteValidator singleton = null;
	
	private ReadWriteValidator() {
		gameMap = new HashMap<Long, AtomicInteger>();
		threadMap = new HashMap<Long, AtomicInteger>();
		threadToGame = new HashMap<Long, Long>();
	}
	
	public static synchronized void initialRead(long gameid) {
		if(singleton == null)
			singleton = new ReadWriteValidator();
		if(singleton.gameMap.containsKey(gameid) == false)
			singleton.gameMap.put(gameid, new AtomicInteger());
		long threadId = Thread.currentThread().getId();
		singleton.threadMap.put(threadId, new AtomicInteger(singleton.gameMap.get(gameid).get()));
		singleton.threadToGame.put(threadId, gameid);
	}
	
	public static synchronized void startWrite(Connection con, long gameid) throws SQLException {
		if(singleton == null)
			singleton = new ReadWriteValidator();
		con.setAutoCommit(false);
		if(singleton.gameMap.containsKey(gameid) == false)
			singleton.gameMap.put(gameid, new AtomicInteger());
	}
	
	public static synchronized void endWrite(Connection con, long gameid) throws SQLException {
		if(singleton == null)
			singleton = new ReadWriteValidator();
		long threadId = Thread.currentThread().getId();
		boolean commit = false;
		if(singleton.threadMap.containsKey(threadId) && singleton.threadToGame.get(threadId) == gameid) {
			if(singleton.threadMap.get(threadId).get() == singleton.gameMap.get(gameid).get())
				commit = true;
		}
		else
			commit = true;
		if(commit) {
			con.commit();
			singleton.threadMap.get(threadId).getAndIncrement();
			singleton.gameMap.get(gameid).getAndIncrement();
		}
		else
			con.rollback();
	}
}
