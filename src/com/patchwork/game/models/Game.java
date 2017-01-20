package com.patchwork.game.models;

import com.patchwork.game.logic.Action;

public class Game {
	public long id;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Player[] players;
	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public GameState state;
	public GameState getState() {
		return state;
	}

	public int isstarted;
	public int getIsstarted() {
		return isstarted;
	}

	public void setIsstarted(int isstarted) {
		this.isstarted = isstarted;
	}

	public int iscomplete;
	public int getIscomplete() {
		return iscomplete;
	}
	
	public boolean isSinglePlayer;
	public boolean getIsSinglePlayer() {
		return isSinglePlayer;
	}

	public Game() {
		state = new GameState(this);
		players = new Player[2];
		this.id = 0;
	}

	public boolean start() {
		if(this.isstarted == 1)
			return false;
		if ( state.start() == false){
			return false;
		}
		this.isstarted = 1;
		return true;
	}

	public int getPatchId() {
		if(this.iscomplete == 0) {
			if(((Action)state.actionQueue.getFirst()).commands.getFirst().parameters.containsKey("patch"))
				return ((Action)state.actionQueue.getFirst()).commands.getFirst().parameters.get("patch");
		}
		return -1;
	}

	public Game cloneGame() {
		Game clone = new Game();
		clone.id = this.id;
		clone.iscomplete = this.iscomplete;
		clone.isstarted = this.isstarted;
		clone.name = this.name;
		for(int i = 0; i<2; i++){
			if(players.length > i && players[i] != null)
				clone.players[i] = players[i].clonePlayer();
		}
		clone.state = this.state.cloneState(clone);
		return clone;
	}
}
