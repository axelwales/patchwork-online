package com.patchwork.mcts;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import com.patchwork.game.logic.Action;
import com.patchwork.game.logic.ActionInvoker;
import com.patchwork.game.logic.Command;
import com.patchwork.game.models.Game;

public class PatchworkDomainPolicy implements DomainPolicy{
	
	Game original;
	Game game;
	VisualTreeDTO visualTree;

	PatchworkDomainPolicy(Game original) {
		this.original = original;
		this.game = original.cloneGame();
		this.visualTree = new VisualTreeDTO();
	}
	
	@Override
	public boolean isTerminal(MCTSNode node) {
		return game.iscomplete == 1;
	}

	@Override
	public boolean canExpand(MCTSNode node) {
		HashMap<String,Command> possibilities = getPossibilities(node);
		if(possibilities.size() > 0)
			return true;
		return false;
	}
	
	private HashMap<String,Command> getPossibilities(MCTSNode node) {
		PatchworkNode n = (PatchworkNode) node;
		Action currentAction = (Action) game.state.actionQueue.peek();
		HashMap<String,Command> possibilities = currentAction.getPossibleCommands(game.state);
		PatchworkAction childAction;
		for( MCTSNode c : n.children ) {
			childAction = (PatchworkAction) c.action;
			possibilities.remove(childAction.getIdString());
		}
		return possibilities;
	}

	@Override
	public MCTSNode doAction(MCTSNode current , boolean appendNode) {
		PatchworkAction a = chooseAction(current);
		doAction(a);
		return createNode(current, a, appendNode);
	}

	private void doAction(PatchworkAction a) {
		ActionInvoker.sendCommand(game.state,  a.command.name, a.command.parametersToString());
	}

	private MCTSNode createNode(MCTSNode parent, PatchworkAction a, boolean appendNode) {
		PatchworkNode result = new PatchworkNode();
		result.action = a;
		result.parent = parent;
		if(parent != null && appendNode == true)
			parent.children.add(result);
		visualTree.add(result, appendNode);
		return result;
	}

	private PatchworkAction chooseAction(MCTSNode current) {
		HashMap<String,Command> possibilities = getPossibilities(current);
		Random rnd = new Random();
		int i = rnd.nextInt(possibilities.values().size());
		Command c = null;
	    for(Command p: possibilities.values()) {
	    	if (--i < 0) {
	    		c = p;
	    		break;
	    	}
	    }
	    PatchworkAction result = new PatchworkAction(c);
		return result;
	}

	@Override
	public double getReward(MCTSNode current) {
		PatchworkReward r = (PatchworkReward) current.reward;
		double reward = r.reward.get(((PatchworkAction)current.action).command.action.player.id);
		return reward;
	}

	@Override
	public MCTSReward setReward(MCTSNode current) {
		PatchworkReward result = new PatchworkReward();
		double reward = 0; 
		if (game.state.currentPlayer.buttons > game.state.nextPlayer.buttons)
			reward = 1;
		else if (game.state.currentPlayer.buttons < game.state.nextPlayer.buttons)
			reward = -1;
		result.reward.put(game.state.currentPlayer.id, reward);
		result.reward.put(game.state.nextPlayer.id, -reward);
		return result;
	}

	@Override
	public void updateReward(MCTSNode current, MCTSReward reward) {
		PatchworkReward toUpdate = (PatchworkReward) current.reward;
		double value = 0;
		for( Entry<Long, Double> kv : ((PatchworkReward)reward).reward.entrySet()) {
			if(toUpdate.reward.containsKey(kv.getKey()))
				value = toUpdate.reward.get(kv.getKey()) + kv.getValue();
			else
				value = kv.getValue();
			toUpdate.reward.put(kv.getKey(), value);
		}
	}

	@Override
	public void updateState(MCTSNode node) {
		doAction((PatchworkAction)node.action);
	}

	@Override
	public double getExplorationConstant() {
		return 2/Math.sqrt(2);
	}

	@Override
	public void resetState() {
		game = original.cloneGame();
	}

	@Override
	public MCTSNode createRoot() {
		return createNode(null, null, false);
	}

}
