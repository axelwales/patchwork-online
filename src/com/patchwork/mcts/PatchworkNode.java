package com.patchwork.mcts;

import java.util.LinkedList;

public class PatchworkNode extends MCTSNode {

	PatchworkNode() {
		this.children = new LinkedList<MCTSNode>();
		this.reward = new PatchworkReward();
	}
}
