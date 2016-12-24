package com.patchwork.game.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.patchwork.game.models.GameState;

public class EndGame extends Command {
	public EndGame(Action action) {
		this.action = action;
		this.name = "endgame";
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
		state.currentPlayer.buttons -= state.currentPlayer.board.numEmpty();
		if(state.currentPlayer.boardBonus == 1)
			state.currentPlayer.buttons += 7;
		state.nextPlayer.buttons -= state.nextPlayer.board.numEmpty();
		if(state.nextPlayer.boardBonus == 1)
			state.nextPlayer.buttons += 7;
		state.game.iscomplete = 1;
		ActionQueueManager.completeAction(state);
	}
	@Override
	public LinkedList<Command> getPossibleCommands(GameState state) {
		return null;
	}
}
