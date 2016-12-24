package com.patchwork.game.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.patchwork.game.models.GameState;
import com.patchwork.game.models.patches.Patch;
import com.patchwork.game.models.patches.Patches;
import com.patchwork.game.models.patches.Point;

public class Place extends Command {
	public Place(Action action) {
		this.action = action;
		this.name = "place";
		this.parameters = new HashMap<String, Integer>();
		String[] parameters = {"patch", "x", "y", "rotations", "flip"};
		this.requiredParameters = new HashSet<String>(parameters.length);
		for (String p : parameters) {
			this.requiredParameters.add(p);
		}
		this.parameters.put("rotations", 0);
		this.parameters.put("flip", 0);
	}
	@Override
	public boolean validate(GameState state) {
		if( super.validateRequiredParameters() == false )
			return false;
		if ( Patches.validatePatchId(this.parameters.get("patch")) == false)
			return false;
		Patch patch = Patches.getPatch(this.parameters.get("patch"));
		int rotation = this.parameters.get("rotations");
		if(rotation > 3)
			return false;
		boolean flip = this.parameters.get("flip") == 1;
		patch.rotate(rotation);
		if(flip)
			patch.flip();
		int x = this.parameters.get("x");
		int y = this.parameters.get("y");
		Point loc = new Point(x,y);
		if(this.action.player.board.validatePlacement(patch, loc)==false)
			return false;
		
		return true;
	}

	@Override
	public void execute(GameState state) {
		Patch patch = Patches.getPatch(this.parameters.get("patch"));
		int rotation = this.parameters.get("rotations");
		patch.rotate(rotation);
		boolean flip = this.parameters.get("flip") == 1;
		if(flip)
			patch.flip();
		int x = this.parameters.get("x");
		int y = this.parameters.get("y");
		Point loc = new Point(x,y);
		this.action.player.board.place(patch, loc);
		if(state.boardBonus == 1) {
			if (this.action.player.board.checkBoardBonus() == true) {
				state.boardBonus = 0;
				this.action.player.boardBonus = 1;
			}
		}
		ActionQueueManager.completeAction(state);
	}
	@Override
	public LinkedList<Command> getPossibleCommands(GameState state) {
		LinkedList<Command> possibilities = new LinkedList<Command>();
		Command c;
		for(int i = 0; i<action.player.board.state.length; i++) {
			for(int j = 0; j<action.player.board.state[i].length; j++) {
				for(int k = 0; k<4; k++) {
					for(int m = 0; m<2; m++){
						c = new Place(this.action);
						c.parameters.put("patch", this.parameters.get("patch"));
						c.parameters.put("x", i);
						c.parameters.put("y", j);
						c.parameters.put("rotations", k);
						c.parameters.put("flip", m);
						if(c.validate(state))
							possibilities.add(c);
					}
				}
			}
		}
		return possibilities;
	}
}
