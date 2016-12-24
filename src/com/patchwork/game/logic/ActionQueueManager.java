package com.patchwork.game.logic;

import java.util.Collection;

import com.patchwork.game.models.GameState;

public class ActionQueueManager {
	public static void updateQueue(GameState state, Action action) {
		completeAction(state);
		state.actionQueue.addFirst(action);
	}
	
	public static void updateQueue(GameState state, Collection<Action> actions) {
		completeAction(state);
		state.actionQueue.addAll(0,actions);
	}
	
	public static void completeAction(GameState state) {
		if(state.actionQueue.size() > 0) {
			Action completedAction = (Action) state.actionQueue.removeFirst();
			completedAction.isComplete = true;
			state.completedActionQueue.add(completedAction);
		}
	}
}
