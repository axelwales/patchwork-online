package com.patchwork.game.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.patchwork.game.models.GameState;
import com.patchwork.game.models.Player;
import com.patchwork.game.models.TimeTrack;

public class EndTurn extends Command {
	public EndTurn(Action action) {
		this.action = action;
		this.name = "endturn";
		this.parameters = new HashMap<String, Integer>();
		this.requiredParameters = new HashSet<String>();
		this.autoComplete = true;
	}
	@Override
	public boolean validate(GameState state) {
		return true;
	}

	@Override
	public void execute(GameState state) {
		if(state.currentPlayer.position > state.nextPlayer.position) {
			Player temp = state.currentPlayer;
			state.currentPlayer = state.nextPlayer;
			state.currentPlayer.currentPlayer = true;
			state.nextPlayer = temp;
			state.nextPlayer.currentPlayer = false;
		}
		Action nextAction = null;
		if(state.currentPlayer.position >= TimeTrack.max && state.nextPlayer.position >= TimeTrack.max) {
			Action endGame = new Action(state);
			endGame.createEndGameAction();
			nextAction = endGame;
		}
		else {
			Action newTurn = new Action(state);
			newTurn.createChooseAction();
			nextAction = newTurn;
		}
		ActionQueueManager.updateQueue(state, nextAction);
	}
	@Override
	public LinkedList<Command> getPossibleCommands(GameState state) {
		return null;
	}
}
