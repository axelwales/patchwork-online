package com.patchwork.mcts;

public interface DomainPolicy {


	boolean isTerminal(MCTSNode node);

	boolean canExpand(MCTSNode node);

	MCTSNode doAction(MCTSNode current, boolean b);

	double getReward(MCTSNode current);

	void updateReward(MCTSNode current, MCTSReward reward);

	void updateState(MCTSNode node);

	MCTSReward setReward(MCTSNode current);

	double getExplorationConstant();

	void resetState();
	
	MCTSNode createRoot();

}
