package com.patchwork.game.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.patchwork.game.models.GameState;
import com.patchwork.game.models.patches.Patches;

public class Rotate extends Command {
	public Rotate(Action action) {
		this.action = action;
		this.name = "rotate";
		this.parameters = new HashMap<String, Integer>();
		String[] parameters = {"patch","rotations"};
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
		if( this.parameters.get("rotations") > 3)
			return false;
		if(action.findCommand("place") == null)
			return false;
		return true;
	}

	@Override
	public void execute(GameState state) {
		Action currentAction = (Action)state.actionQueue.getFirst();
		Command place = currentAction.findCommand("place");
		int newRotation = this.parameters.get("rotations");
		if(place.parameters.containsKey("rotations")) {
			int currentRotation = place.parameters.get("rotations");
			newRotation = (currentRotation + newRotation)%4;			
		}
		place.parameters.put("rotations",newRotation);
	}
	@Override
	public LinkedList<Command> getPossibleCommands(GameState state) {
		return null;
	}
}
