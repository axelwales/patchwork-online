package com.patchwork.mcts;

import java.util.LinkedList;

public class VisualTreeDTO {

	LinkedList<VisualNodeDTO> accessOrder;
	VisualNodeDTO current;
	boolean inDefaultPolicy;
	
	public VisualTreeDTO () {
		accessOrder = new LinkedList<VisualNodeDTO>();
		current = null;
		inDefaultPolicy = false;
	}
	
	public void updateTree(PatchworkNode toAdd, boolean isNewNode, boolean isTreeNode) {
		if(isNewNode && isTreeNode) {
			addToTree(toAdd);
			accessOrder.add( new VisualNodeDTO(toAdd));
		} else if(!isNewNode && isTreeNode) {
			if(inDefaultPolicy == false) {
				descendTree(toAdd);
				accessOrder.add( new VisualNodeDTO(toAdd));
			} else { //just returned from default policy
				resetTree();
				accessOrder.add( new VisualNodeDTO(toAdd));
			}
		} else if(!isNewNode && !isTreeNode) {
			inDefaultPolicy = true;
			current.defaultIterations++;
		}
	}
	
	private void resetTree() {
		inDefaultPolicy = false;
		current = accessOrder.getFirst();
	}

	private void descendTree(PatchworkNode toAdd) {
		if(current.children.size() == 0)
			return;
		int i = 0;
		PatchworkAction addAction = (PatchworkAction) toAdd.action;
		PatchworkAction childAction;
		for(MCTSNode c : toAdd.parent.children ){
			childAction = (PatchworkAction) c.action;
			if(addAction.command.getIdString().equals(childAction.command.getIdString())) {
				current = current.children.get(i);
				break;
			}
		}
	}

	public void addToTree(PatchworkNode toAdd) {
		VisualNodeDTO newNode = new VisualNodeDTO(toAdd);
		newNode.parent = current;
		if(current != null)
			current.children.add(newNode);
		current = newNode;
	}
}
