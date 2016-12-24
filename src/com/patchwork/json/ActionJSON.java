package com.patchwork.json;

import org.json.simple.JSONObject;

import com.patchwork.game.logic.Command;

public class ActionJSON {

	@SuppressWarnings("unchecked")
	public static String chooseSuccess(String clientVar, String patchList, String playerStats) {
		JSONObject result = basicSuccess();
		result.put("client", clientVar);
		result.put("patches", patchList);
		result.put("stats", playerStats);
		return result.toJSONString();
	}

	@SuppressWarnings("unchecked")
	private static JSONObject basicSuccess() {
		JSONObject result = new JSONObject();
		result.put("success", true);
		return result;
	}

	@SuppressWarnings("unchecked")
	public static String placeSuccess(String clientVar, String playerBoard) {
		JSONObject result = basicSuccess();
		result.put("client", clientVar);
		result.put("board", playerBoard);
		return result.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static String actionFailure() {
		JSONObject result = new JSONObject();
		result.put("success", false);
		return result.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static String updateSuccess(String container) {
		JSONObject result = basicSuccess();
		result.put("container", container);
		return result.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static String aiSuccess(Command c) {
		JSONObject result = basicSuccess();
		result.put("commandName", c.name);
		result.put("commandParams", c.parametersToString());
		return result.toJSONString();
	}

}
