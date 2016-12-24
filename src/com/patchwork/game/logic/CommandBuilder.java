package com.patchwork.game.logic;

import com.patchwork.game.models.GameState;

public class CommandBuilder {
	
	public static Command[] build(String[] commandStrings, GameState state) {
		String commandName = commandStrings[0];
		String commandParams = commandStrings[1];
		Command c = buildCommand((Action) state.actionQueue.peek(), (long) 0, commandName, commandParams );
		if(c == null)
			return null;
		Command[] result = new Command[] {c};
		return result;
	}
	
	public static Command buildCommand(Action a, Long id, String name, String params) {
		Command c = null;
		switch(name) {
			case "choose":
				c = new Choose(a);
				break;
			case "endgame":
				c = new EndGame(a);
				break;
			case "endturn":
				c = new EndTurn(a);
				break;
			case "flip":
				c = new Flip(a);
				break;
			case "place":
				c = new Place(a);
				break;
			case "rotate":
				c = new Rotate(a);
				break;		
		}
		if( c == null )
			return c;
		if(id == -1)
			return c;
		c.id = id;
		if(params == null)
			return c;
		c.parametersFromString(params);
		return c;
	}

}
