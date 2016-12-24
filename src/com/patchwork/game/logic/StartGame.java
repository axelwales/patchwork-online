package com.patchwork.game.logic;

import com.patchwork.game.models.GameState;

public class StartGame{
	
	public static boolean startGame(GameState state) {
		if( state.game.start() == false )
			return false;
		Action startAction = new Action(state);
		startAction.createChooseAction();
		ActionQueueManager.updateQueue(state, startAction);
		return true;
	}

}
