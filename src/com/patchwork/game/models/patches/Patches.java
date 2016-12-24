package com.patchwork.game.models.patches;

import java.util.HashMap;

public class Patches {
	public static final Patch bonus = new Patch(0, 0, 0, 0, 1, new int[][]{{0,0}});
	public static final Patch[] patches = new Patch[34];
	public static final HashMap<Integer,Patch> patchesById = new HashMap<Integer,Patch>();
	
	static {
		patches[0] = new Patch(0, 0, 0, 0, 1, new int[][]{{0,0}});
		patchesById.put(0, patches[0]);
		
		patches[1] = new Patch(1, 5, 5, 2, 5, new int[][]{{0,0},{-1,0},{1,0},{0,1},{0,2}});
		patchesById.put(1, patches[1]);
		
		patches[2] = new Patch(2, 1, 2, 0, 6, new int[][]{{0,0},{-1,0},{0,1},{0,2},{0,3},{1,3}});
		patchesById.put(2, patches[2]);
		
		patches[3] = new Patch(3, 2, 1, 0, 6, new int[][]{{0,0},{-1,0},{0,-1},{0,1},{0,2},{1,1}});
		patchesById.put(3, patches[3]);
		
		patches[4] = new Patch(4, 10, 5, 3, 6, new int[][]{{0,0},{1,0},{0,1},{1,1},{1,2},{1,3}});
		patchesById.put(4, patches[4]);
		
		patches[5] = new Patch(5, 2, 2, 0, 3, new int[][]{{0,0},{0,1},{0,2}});
		patchesById.put(5, patches[5]);
		
		patches[6] = new Patch(6, 5, 3, 1, 8, new int[][]{{0,0},{0,1},{1,0},{1,1},{0,2},{1,2},{-1,1},{2,1}});
		patchesById.put(6, patches[6]);
		
		patches[7] = new Patch(7, 0, 3, 1, 6, new int[][]{{0,0},{-1,0},{1,0},{2,0},{1,1},{1,-1}});
		patchesById.put(7, patches[7]);
		
		patches[8] = new Patch(8, 7, 1, 1, 5, new int[][]{{0,0},{1,0},{2,0},{3,0},{4,0}});
		patchesById.put(8, patches[8]);
		
		patches[9] = new Patch(9, 10, 4, 3, 5, new int[][]{{0,0},{-1,0},{0,1},{1,1},{1,2}});
		patchesById.put(9, patches[9]);
		
		patches[10] = new Patch(10, 7, 4, 2, 6, new int[][]{{0,0},{-1,0},{0,1},{1,1},{1,0},{2,0}});
		patchesById.put(10, patches[10]);
		
		patches[11] = new Patch(11, 6, 5, 2, 4, new int[][]{{0,0},{0,1},{1,1},{1,0}});
		patchesById.put(11, patches[11]);
		
		patches[12] = new Patch(12, 8, 6, 3, 6, new int[][]{{0,0},{-1,0},{0,1},{1,1},{1,2},{0,2}});
		patchesById.put(12, patches[12]);
		
		patches[13] = new Patch(13, 7, 6, 3, 4, new int[][]{{0,0},{-1,0},{0,1},{1,1}});
		patchesById.put(13, patches[13]);
		
		patches[14] = new Patch(14, 3, 6, 2, 6, new int[][]{{0,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1}});
		patchesById.put(14, patches[14]);
		
		patches[15] = new Patch(15, 2, 3, 1, 5, new int[][]{{0,0},{1,0},{1,1},{1,2},{1,3}});
		patchesById.put(15, patches[15]);
		
		patches[16] = new Patch(16, 7, 2, 2, 6, new int[][]{{0,0},{-1,0},{1,0},{0,1},{0,2},{0,3}});
		patchesById.put(16, patches[16]);
		
		patches[17] = new Patch(17, 2, 2, 0, 5, new int[][]{{0,0},{1,0},{1,1},{0,1},{0,2}});
		patchesById.put(17, patches[17]);
		
		patches[18] = new Patch(18, 10, 3, 2, 5, new int[][]{{0,0},{0,1},{1,0},{2,0},{3,0}});
		patchesById.put(18, patches[18]);
		
		patches[19] = new Patch(19, 1, 2, 0, 5, new int[][]{{0,0},{0,1},{1,0},{2,1},{2,0}});
		patchesById.put(19, patches[19]);
		
		patches[20] = new Patch(20, 4, 6, 2, 4, new int[][]{{0,0},{1,1},{1,0},{1,2}});
		patchesById.put(20, patches[20]);
		
		patches[21] = new Patch(21, 4, 2, 0, 6, new int[][]{{0,0},{1,1},{1,0},{0,1},{0,-1},{1,2}});
		patchesById.put(21, patches[21]);
		
		patches[22] = new Patch(22, 3, 1, 0, 3, new int[][]{{0,0},{1,1},{1,0}});
		patchesById.put(22, patches[22]);
		
		patches[23] = new Patch(23, 1, 4, 1, 7, new int[][]{{0,0},{-1,0},{1,0},{1,1},{1,-1},{2,0},{3,0}});
		patchesById.put(23, patches[23]);
		
		patches[24] = new Patch(24, 1, 5, 1, 6, new int[][]{{0,0},{1,0},{1,1},{1,2},{1,3},{0,3}});
		patchesById.put(24, patches[24]);
		
		patches[25] = new Patch(25, 3, 2, 1, 4, new int[][]{{0,0},{1,0},{1,1},{2,1}});
		patchesById.put(25, patches[25]);
		
		patches[26] = new Patch(26, 2, 3, 0, 7, new int[][]{{0,0},{1,0},{2,0},{0,1},{0,-1},{2,1},{2,-1}});
		patchesById.put(26, patches[26]);
		
		patches[27] = new Patch(27, 3, 4, 1, 5, new int[][]{{0,0},{-1,0},{1,0},{2,0},{1,1}});
		patchesById.put(27, patches[27]);
		
		patches[28] = new Patch(28, 4, 2, 1, 4, new int[][]{{0,0},{-1,0},{0,1},{0,2}});
		patchesById.put(28, patches[28]);
		
		patches[29] = new Patch(29, 5, 4, 2, 5, new int[][]{{0,0},{-1,0},{1,0},{0,1},{0,-1}});
		patchesById.put(29, patches[29]);
		
		patches[30] = new Patch(30, 3, 3, 1, 4, new int[][]{{0,0},{1,0},{2,0},{3,0}});
		patchesById.put(30, patches[30]);
		
		patches[31] = new Patch(31, 2, 2, 0, 4, new int[][]{{0,0},{1,0},{2,0},{1,1}});
		patchesById.put(31, patches[31]);
		
		patches[32] = new Patch(32, 1, 3, 0, 3, new int[][]{{0,0},{1,0},{1,1}});
		patchesById.put(32, patches[32]);
		
		patches[33] = new Patch(33, 2, 1, 0, 2, new int[][]{{0,0},{1,0}});
		patchesById.put(33, patches[33]);
	}
	
	public static boolean validatePatchId(int id) {
		return patchesById.containsKey(id);
	}
	public static Patch getPatch(int id) {
		if(Patches.validatePatchId(id)) {
			Patch p = patchesById.get(id);
			return clonePatch(p);
		}
		return null;
	}
	
	private static Patch clonePatch(Patch p) {
		return new Patch(p.staticId, p.cost, p.time, p.income, p.size, p.map);
	}
	
	public static Patch[] getAll() {
		Patch[] result = new Patch[patches.length];
		int i = 0;
		for (Patch p : patches) {
			result[i] = clonePatch(p);
			i++;
		}
		return result;
	}
	public static Patch[] getStartPatches() {
		Patch[] p = getAll();
		Patch[] result = new Patch[p.length-1];
		for(int i = 1; i<p.length; i++) //start at 1 to exclude bonus patch
			result[i-1] = p[i];
		return result;
	}
}
