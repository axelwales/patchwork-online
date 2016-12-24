package com.patchwork.game.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.patchwork.game.models.GameState;

public abstract class Command {
	public Long id = (long) 0;
	public Action action;
	public String name;
	public String getName() {
		return name;
	}
	public HashMap<String, Integer> getParameters() {
		return parameters;
	}

	public HashMap<String,Integer > parameters;
	public HashSet<String> requiredParameters;
	public boolean isCompleted = false;
	public boolean autoComplete = false;
	
	public abstract boolean validate(GameState state);
	public abstract void execute(GameState state);
	
	public boolean validateRequiredParameters() {
		for( String key : requiredParameters) {
			if(this.parameters.containsKey(key) == false)
				return false;
		}
		return true;
	}
	
	public String parametersToString() {
		String result = "";
		int i = 0;
		for(Entry<String,Integer> param : parameters.entrySet()) {
			result += param.getKey() + ":" + param.getValue();
			if(i != parameters.size() - 1)
				result+= ";";
			i++;
		}
		return result;
	}
	
	public void parametersFromString(String paramString) {
		String[] params = paramString.split(";");
		for( String param : params) {
			if(!"".equals(param)) {
				String[] keyValue = param.split(":");
				parameters.put(keyValue[0], Integer.parseInt(keyValue[1]));
			}
		}
	}
	
	public abstract LinkedList<Command> getPossibleCommands(GameState state);
	public String getIdString() {
		return this.name + "::" + this.parametersToString();
	}
	public Command cloneCommand(Action actionClone) {
		return CommandBuilder.buildCommand(actionClone, this.id, this.name, this.parametersToString());
	}
}