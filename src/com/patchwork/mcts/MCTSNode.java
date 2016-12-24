package com.patchwork.mcts;

import java.util.LinkedList;

public class MCTSNode {
	MCTSNode parent;
	LinkedList<MCTSNode> children;
	MCTSAction action;
	MCTSReward reward;
	public int visits;
}
