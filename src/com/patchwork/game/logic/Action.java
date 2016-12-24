package com.patchwork.game.logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.patchwork.game.models.ActionInterface;
import com.patchwork.game.models.Game;
import com.patchwork.game.models.GameState;
import com.patchwork.game.models.Player;
//TODO create and set auto complete flag
public class Action implements ActionInterface {
	public long id;
	public Player player;
	public LinkedList<Command> commands;
	public LinkedList<Command> completeCommands;
	public boolean isComplete;
	
	public Action(GameState state){
		this();
		this.player = state.currentPlayer;
	}
	public Action(Player player){
		this();
		this.player = player;
	}
	public Action() {
		this.commands = new LinkedList<Command>();
		this.completeCommands = new LinkedList<Command>();
		this.id = 0;
	}
	public Command findCommand(String name) {
		for (Command com : commands) {
			if(com.name.equals(name))
				return com;
		}
		return null;
	}
	
	public void update(Command com) {
		Command update = findCommand(com.name);
		if(update != null){
			Set<String> keys = com.parameters.keySet();
			for( String key : keys) {
				if(!key.equals("patch"))
					update.parameters.put(key, com.parameters.get(key));		
			}
		}
	}
	
	public void createPlaceAction(int patchId) {
		Command rotate = new Rotate(this);
		rotate.parameters.put("patch", patchId);
		Command flip = new Flip(this);
		flip.parameters.put("patch", patchId);
		Command place = new Place(this);
		place.parameters.put("patch", patchId);
		this.commands.add(rotate);
		this.commands.add(flip);
		this.commands.add(place);
	}
	
	public void createEndTurnAction() {
		Command endTurn = new EndTurn(this);
		this.commands.add(endTurn);
	}
	
	public void createChooseAction() {
		Command choose = new Choose(this);
		this.commands.add(choose);
	}
	
	public void createEndGameAction() {
		Command endGame = new EndGame(this);
		this.commands.add(endGame);
	}
	public void getParameters(Command command) {
		Command update = findCommand(command.name);
		if(update != null){
			Set<String> keys = update.parameters.keySet();
			for( String key : keys) {
				if(!command.parameters.containsKey(key))
					command.parameters.put(key, update.parameters.get(key));		
			}
		}
	}
	public LinkedList<Command> getCommands() {
		return commands;
	}
	public void execute(Command command, GameState state) {
		Command toExecute = findCommand(command.name);
		toExecute.execute(state);
		commands.remove(toExecute);
		toExecute.isCompleted = true;
		this.completeCommands.add(toExecute);
	}
	public boolean autoExecute(GameState state) {
		Command c = commands.peek();
		if( c.name.equals("endturn") || c.name.equals("endgame") ) {
			execute(c, state);
			return true;
		}
		return false;
	}
	
	public HashMap<String, Command> getPossibleCommands(GameState state) {
		HashMap<String, Command> result = new HashMap<String, Command>();
		String idString;
		for(Command c : commands) {
			LinkedList<Command> possibilities = c.getPossibleCommands(state);
			if(possibilities != null) {
				for(Command p : possibilities) {
					idString = p.getIdString();
					result.put(idString, p);
				}
			}
		}
		return result;
	}
	public Action cloneAction(Game gameClone) {
		Player player = null;
		for( Player p : gameClone.players) {
			if(p.id == this.player.id)
				player = p;
		}
		Action clone = new Action(player);
		clone.id = this.id;
		clone.isComplete = this.isComplete;
		for(Command c : this.commands) {
			clone.commands.add(c.cloneCommand(clone));
		}
		return clone;
	}
}
