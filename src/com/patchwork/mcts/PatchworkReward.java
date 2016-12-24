package com.patchwork.mcts;

import java.util.HashMap;

public class PatchworkReward implements MCTSReward {

	public HashMap<Long,Double> reward;
	
	public PatchworkReward() {
		this.reward = new HashMap<Long,Double>();
	}

}
