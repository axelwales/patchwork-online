package com.patchwork.game.models;

import java.util.LinkedList;
import java.util.Random;

import com.patchwork.game.logic.Action;
import com.patchwork.game.models.patches.Patch;
import com.patchwork.game.models.patches.PatchList;
import com.patchwork.game.models.patches.Patches;

public class GameState {
	public Game game;
	public Player currentPlayer;
	public Player nextPlayer;
	public LinkedList<ActionInterface> actionQueue;
	public LinkedList<ActionInterface> getActionQueue() {
		return actionQueue;
	}

	public LinkedList<ActionInterface> completedActionQueue;
	public PatchList patches;
	public PatchList getPatches() {
		return patches;
	}

	public TimeTrack track;
	public TimeTrack getTrack() {
		return track;
	}
	
	public int boardBonus;
	public int getBoardBonus() {
		return boardBonus;
	}

	public GameState(Game game) {
		this.game = game;
		actionQueue = new LinkedList<ActionInterface>();
		completedActionQueue = new LinkedList<ActionInterface>();
		track = new TimeTrack();
		boardBonus = 1;
	}
	
	public boolean start() {
		if ( track == null)
			track = new TimeTrack();
		shuffle(game.players);
		currentPlayer = game.players[0];
		currentPlayer.currentPlayer = true;
		currentPlayer.board = new PlayerBoard();
		nextPlayer = game.players[1];
		nextPlayer.currentPlayer = false;
		nextPlayer.board = new PlayerBoard();
		Patch[] startPatches = Patches.getStartPatches();
		shuffle(startPatches);
		patches = new PatchList(startPatches);
		return patches.start();
	}
	
	public static void shuffle(Object[] toShuffle) {
		Random r = new Random();
		int j;
		Object temp;
		for( int i = toShuffle.length - 1; i > 0; i-- ) {
			j = r.nextInt(i);
			temp = toShuffle[j];
			toShuffle[j] = toShuffle[i];
			toShuffle[i] = temp;
		}
	}

	public GameState cloneState(Game gameClone) {
		GameState clone = new GameState(gameClone);
		clone.boardBonus = this.boardBonus;
		for(Player p : gameClone.players) {
			if(p.currentPlayer == true)
				clone.currentPlayer = p;
			else
				clone.nextPlayer = p;
		}
		Action a;
		for(ActionInterface ai : this.actionQueue) {
			a = (Action) ai;
			clone.actionQueue.add(a.cloneAction(gameClone));
		}
		clone.patches = this.patches.clonePatches();
		clone.track = this.track.cloneTrack();
			
		return clone;
	}
}
