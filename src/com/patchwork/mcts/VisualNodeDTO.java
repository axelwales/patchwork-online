package com.patchwork.mcts;

import java.util.LinkedList;

public class VisualNodeDTO {

	public VisualNodeDTO(PatchworkNode toAdd) {
		children = new LinkedList<VisualNodeDTO>();
		defaultIterations = 0;
	}
	
	public LinkedList<VisualNodeDTO> children;
	public VisualNodeDTO parent;
	public int defaultIterations;

}
