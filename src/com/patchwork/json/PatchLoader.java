package com.patchwork.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.patchwork.game.models.patches.Patch;
import com.patchwork.game.models.patches.Patches;
import com.patchwork.game.models.patches.Point;

public class PatchLoader {
	
	@SuppressWarnings("unchecked")
	public static String getPatchesAsJSON() {
		Patch[] patches = Patches.getAll();
		JSONObject result = new JSONObject();
		
		for( Patch p : patches ) {
			JSONObject pj = buildPatchJSON(p);
			result.put("p" + p.staticId, pj);
		}
		return result.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject buildPatchJSON(Patch p) {
		JSONObject result = new JSONObject();
		result.put("id", p.staticId);
		
		JSONArray map = new JSONArray();
		JSONArray point;
		for ( Point pt : p.map ) {
			point = new JSONArray();
			point.add(pt.x);
			point.add(pt.y);
			map.add(point);
		}
		
		result.put("map", map);
		result.put("cost", p.cost);
		result.put("time", p.time);
		result.put("income", p.income);
		
		return result;
	}
}
