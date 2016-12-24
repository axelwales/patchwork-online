package com.patchwork.game.models;

import java.util.HashMap;

public class GameConstants {
	public static final HashMap<String, Object> gameConstants = new HashMap<String, Object>();
	
	static {
		gameConstants.put("income", TimeTrack.incomeSet);
		gameConstants.put("bonuses", TimeTrack.bonusSet);
	}
}
