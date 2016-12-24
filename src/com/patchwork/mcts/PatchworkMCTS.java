package com.patchwork.mcts;

import com.patchwork.game.logic.Command;
import com.patchwork.game.models.Game;

public class PatchworkMCTS {
	static PatchworkDomainPolicy pdp;

	public static Command getCommand(Game game) {
		pdp = new PatchworkDomainPolicy(game);
		PatchworkNode root = (PatchworkNode) pdp.createRoot();
		root = (PatchworkNode) MCTS.doSearch(root, pdp, 500);
		return ((PatchworkAction)root.action).command;
	}
	
}
