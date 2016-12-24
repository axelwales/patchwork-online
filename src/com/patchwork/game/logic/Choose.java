package com.patchwork.game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.patchwork.game.models.GameState;
import com.patchwork.game.models.TimeTrack;
import com.patchwork.game.models.patches.Patch;
import com.patchwork.game.models.patches.Patches;

public class Choose extends Command {
	
	public Choose(Action action) {
		this.action = action;
		this.name = "choose";
		this.parameters = new HashMap<String, Integer>();
		String[] parameters = {"choice"};
		this.requiredParameters = new HashSet<String>(parameters.length);
		for (String p : parameters) {
			this.requiredParameters.add(p);
		}
	}
	@Override
	public boolean validate(GameState state) {
		if( super.validateRequiredParameters() == false )
			return false;
		int choice = this.parameters.get("choice");
		if(choice > 3 || choice < 0)
			return false;
		if(choice != 3) {
			Patch[] choices = state.patches.getChoices();
			if( choices.length <= choice)
				return false;
			Patch p = choices[choice];
			if(p.cost > this.action.player.buttons)
				return false;
			if(canPlace(p, state) == false) {
				return false;
			}
		}
		return true;
	}

	private boolean canPlace(Patch p, GameState state) {
		// TODO Auto-generated method stub
		Command place = new Place(this.action);
		place.parameters.put("patch", p.staticId);
		LinkedList<Command> possibilities = place.getPossibleCommands(state);
		if( possibilities.size() == 0 )
			return false;
		return true;
	}
	
	@Override
	public void execute(GameState state) {
		int choice = this.parameters.get("choice");
		ArrayList<Action> actions = new ArrayList<Action>();
		int numBonuses, numIncome;
		if(choice != 3) {
			Patch patch = state.patches.choose(choice);
			action.player.buttons -= patch.cost;
			action.player.position += patch.time;
			action.player.income += patch.income;
			
			numBonuses = state.track.getBonuses(action.player.position - patch.time, action.player.position);
			numIncome = TimeTrack.numIncomes(action.player.position - patch.time, action.player.position);
			
			Action placeAction = new Action(state);
			placeAction.createPlaceAction(patch.staticId);
			actions.add(placeAction);
		}
		else {
			int buttonIncome = state.nextPlayer.position - state.currentPlayer.position + 1;
			if (state.currentPlayer.position + buttonIncome > TimeTrack.max)
				buttonIncome = TimeTrack.max - state.currentPlayer.position;
			state.currentPlayer.buttons += buttonIncome;
			state.currentPlayer.position += buttonIncome;
			
			numBonuses = state.track.getBonuses(action.player.position - buttonIncome, action.player.position);
			numIncome = TimeTrack.numIncomes(action.player.position - buttonIncome, action.player.position);
		}
		
		action.player.buttons += action.player.income*numIncome;
		for(int i = 0; i<numBonuses; i++) {
			Action bonusAction = new Action(state);
			bonusAction.createPlaceAction(Patches.bonus.staticId);
			actions.add(bonusAction);
		}
		
		Action endTurn = new Action(state);
		endTurn.createEndTurnAction();
		actions.add(endTurn);
		ActionQueueManager.updateQueue(state, actions);
	}
	@Override
	public LinkedList<Command> getPossibleCommands(GameState state) {
		LinkedList<Command> possibilities = new LinkedList<Command>();
		Command c;
		for(int i = 0; i<4; i++) {
			c = new Choose(this.action);
			c.parameters.put("choice", i);
			if(c.validate(state))
				possibilities.add(c);
		}
		return possibilities;
	}
}
