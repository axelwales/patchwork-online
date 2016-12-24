package com.patchwork.mcts;

import com.patchwork.game.logic.Command;

public class PatchworkAction implements MCTSAction {
	Command command;
	
	public PatchworkAction(Command command) {
		this.command = command;
	}

	public String getIdString() {
		return this.command.getIdString();
	}
}
