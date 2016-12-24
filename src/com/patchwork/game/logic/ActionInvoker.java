package com.patchwork.game.logic;

import com.patchwork.game.models.GameState;

public class ActionInvoker {
	GameState state;
	
	public static Boolean sendCommand(GameState state, String commandName, String commandParams) {
		String[] commandStrings = new String[] { commandName, commandParams };
		ActionInvoker ai = new ActionInvoker();
		ai.state = state;
		ActionRequest ar = new ActionRequest();
		ar.commandStrings = commandStrings;
		if(ai.validate(ar)) {
			ai.execute(ar);
			return true;
		}
		return false;
	}
	
	public boolean validate(ActionRequest request){
		Command[] commands = CommandBuilder.build(request.commandStrings, state);
		if(commands.length == 0)
			return false;
		Action action = (Action) state.actionQueue.getFirst();
		boolean inQueue;
		for ( Command command : commands) {
			inQueue = false;
			for( Command actionCommand : action.commands) {
				if(command.name.equals(actionCommand.name))
						inQueue = true;
			}
			if(inQueue == false)
				return false;
			action.getParameters(command);
			if(command.validate(state)==false)
				return false;
		}
		return true;
	}
	
	public void execute(ActionRequest request){
		Command[] commands = CommandBuilder.build(request.commandStrings, state);
		Action action = (Action) state.actionQueue.getFirst();
		for ( Command command : commands) {
			action.update(command);
			action.execute(command,state);
		}
		boolean autoExecute = true;
		while (autoExecute) {
			if(state.game.iscomplete == 0) {
				Action nextAction = (Action) state.actionQueue.getFirst();
				autoExecute = nextAction.autoExecute(state);
			}
			else
				autoExecute = false;
		}
	}
}
