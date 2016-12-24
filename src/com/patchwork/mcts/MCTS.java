package com.patchwork.mcts;

public class MCTS {
	private MCTSNode root;
	private DomainPolicy dPolicy;
	
	public static MCTSNode doSearch(MCTSNode root, DomainPolicy dPolicy, int iterations) {
		MCTS m = new MCTS();
		m.root = root;
		m.dPolicy = dPolicy;
		return m.search(iterations);
	}
	
	public MCTSNode search(int iterations) {
		MCTSNode current;
		MCTSReward reward;
		while (iterations > 0) {
			current = treePolicy(root);
			reward = defaultPolicy(current);
			backup(current,reward);
			iterations--;
		}
		return bestChild(root, 0);
	}

	private MCTSNode bestChild(MCTSNode node, double cp) {
		MCTSNode bestChild = null;
		double bestValue = -1;
		double value;
		for(MCTSNode child : node.children) {
			value = ((dPolicy.getReward(child)/child.visits) + 1)/2 + cp*Math.sqrt(2*Math.log(node.visits)/child.visits);
			if( value > bestValue) {
				bestValue = value;
				bestChild = child;
			}
		}
		return bestChild;
	}

	private void backup(MCTSNode current, MCTSReward reward) {
		while(current != null) {
			current.visits++;
			dPolicy.updateReward(current,reward);
			current = current.parent;
		}		
	}

	private MCTSReward defaultPolicy(MCTSNode current) {
		while(dPolicy.isTerminal(current) == false) {
			current = dPolicy.doAction(current, false);
		}
		return dPolicy.setReward(current);
	}

	private MCTSNode treePolicy(MCTSNode node) {
		this.dPolicy.resetState();
		while(this.dPolicy.isTerminal(node) == false) {
			if(this.dPolicy.canExpand(node) == true)
				return expand(node);
			else {
				node = bestChild(node, this.dPolicy.getExplorationConstant());
				dPolicy.updateState(node);
			}
		}
		return node;
	}

	private MCTSNode expand(MCTSNode node) {
		return dPolicy.doAction(node, true);
	}
}
