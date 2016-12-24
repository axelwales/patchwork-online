package com.patchwork.game.models;

import java.util.LinkedList;

import com.patchwork.game.models.patches.Patch;
import com.patchwork.game.models.patches.Patches;
import com.patchwork.game.models.patches.Point;

public class PlayerBoard {
	public int[][] state;
	public int[][] getState() {
		return state;
	}
	
	public Patch[] patches;
	
	public PlayerBoard() {
		state = new int[9][9];
		for(int i = 0; i<9; i++) {
			for(int j = 0; j<9; j++)
				state[i][j] = 0;
		}
		patches = new Patch[0];
	}
	public PlayerBoard(String boardArrayString) {
		this();
		this.fromDataString(boardArrayString);
	}
	
	public boolean validatePlacement(Patch p, Point loc){		
		for(Point point: p.map) {
			int x = point.x+loc.x;
			if(x > state.length - 1 || x < 0)
				return false;
			int y = point.y+loc.y;
			if(y > state.length - 1 || y < 0)
				return false;
			int isOccupied = state[x][y];
			if(isOccupied == 1)
				return false;
		}
		return true;
	}
	public void place(Patch p, Point loc) {
		for(Point point: p.map) {
			int x = point.x+loc.x;
			int y = point.y+loc.y;
			state[x][y] = 1;
		}
		p.location = loc;
		this.addToPatches(p);
	}
	
	private void addToPatches(Patch p) {
		Patch[] newArray = new Patch[this.patches.length+1];
		int i = 0;
		for( Patch q : patches ) {
			newArray[i] = q;
			i++;
		}
		newArray[i] = p;
		patches = newArray;
	}
	
	public int numEmpty() {
		int result = 0;
		for(int[] i : this.state) {
			for(int j : i) {
				if(j == 0)
					result++;
			}
		}
		return result;
	}
	
	public boolean checkBoardBonus() {
		boolean iFail = false;
		int iCounter = 0;
		int jCounter = 0;
		int minJCounter = 0;
		int jStart = 0;
		int iStart = 0;
		for(int i = 0; i<this.state.length; i++) {
			for( int j = 0; j<this.state[i].length; j++) {
				if(state[i][j] == 1) {
					if(jCounter == 0)
						jStart = j;
					jCounter++;
				} else {
					if(jCounter < 7)
						jCounter = 0;
					if(j > 1)
						break;
				}
			}
			if(jCounter >= 7) {
				if(iCounter == 0) {
					iStart = jStart;
					minJCounter = jCounter;
				}
				if(iStart < jStart) {
					if(jStart - iStart > minJCounter - jCounter)
						iFail = true;
					iStart = jStart;
				}
				if(iStart > jStart) {
					if(jStart - iStart < minJCounter - jCounter)
						iFail = true;
				}
			}
			else {
				iFail = true;
			}
			if(iFail == true) {
				if(iCounter < 7)
					iCounter = 0;
				if(i > 1)
					break;
			} else
				iCounter++;
			if(iCounter >= 7)
				return true;
			if(jCounter < minJCounter)
				minJCounter = jCounter;
			jCounter = 0;
			iFail = false;
		}
		return false;
	}
	
	public String toDataString() {
		String result = "";
		for(int i =0; i<this.state.length; i++) {
			for(int j = 0; j<this.state[i].length; j++) {
				result += this.state[i][j];
				if(i*9+j != 80)
					result += ",";
			}
		}
		return result;
	}
	public void fromDataString(String arrayString) {
		String[] splitString = arrayString.split(",");
		for(int i =0; i<this.state.length; i++) {
			for(int j = 0; j<this.state[i].length; j++) {
				this.state[i][j] = Integer.parseInt(splitString[i*9+j]);
			}
		}
	}
	public PlayerBoard cloneBoard() {
		PlayerBoard clone = new PlayerBoard(this.toDataString());
		LinkedList<Patch> patchesClone = new LinkedList<Patch>();
		for( Patch p : patches) {
			patchesClone.add(Patches.getPatch(p.staticId));
		}
		clone.patches = patchesClone.toArray(new Patch[patchesClone.size()]);
		return clone;
	}
}
