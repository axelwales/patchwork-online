package com.patchwork.game.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.patchwork.game.models.GameState;
import com.patchwork.game.models.patches.Patches;

public class Flip extends Command {
	public Flip(Action action) {
		this.action = action;
		this.name = "flip";
		this.parameters = new HashMap<String, Integer>();
		String[] parameters = {"patch"};
		this.requiredParameters = new HashSet<String>(parameters.length);
		for (String p : parameters) {
			this.requiredParameters.add(p);
		}
	}
	@Override
	public boolean validate(GameState state) {
		if( super.validateRequiredParameters() == false )
			return false;
		if ( Patches.validatePatchId(this.parameters.get("patch")) == false)
			return false;
		if(action.findCommand("place") == null)
			return false;
		return true;
	}

	@Override
	public void execute(GameState state) {
		Action currentAction = (Action)state.actionQueue.getFirst();
		Command place = currentAction.findCommand("place");
		int newFlip = 1;
		if(place.parameters.containsKey("flip")) {
			int currentFlip = place.parameters.get("flip");
			newFlip = 1 - currentFlip;			
		}
		place.parameters.put("flip",newFlip);
	}
	@Override
	public LinkedList<Command> getPossibleCommands(GameState state) {
		return null;
	}
}
