package com.patchwork.game.models;

import java.util.HashSet;

public class TimeTrack {
	public static final int[] income = new int[] {5,11,17,23,29,35,41,47,53};
	public static final HashSet<Integer> incomeSet = new HashSet<Integer>();
	public static final int[] bonuses = new int[] {20,26,32,44,50};
	public static final HashSet<Integer> bonusSet = new HashSet<Integer>();
	public static final int max = 53;
	
	static {
		for ( int i : income )
			incomeSet.add(i);
		for ( int i : bonuses )
			bonusSet.add(i);
	}
	
	public long id;
	public int nextUnclaimed;
	public int getNextUnclaimed() {
		return nextUnclaimed;
	}

	public int[] bonusClaimed;
	
	public TimeTrack() {
		this.id = 0;
		nextUnclaimed = bonuses[0];
		bonusClaimed = new int[bonuses.length];
		for(int i = 0; i < bonusClaimed.length; i++) {
			bonusClaimed[i] = 0;
		}
	}
	
	public static int numIncomes(int start, int end) {
		int result = 0;
		for( int location : income) {
			if(end < location )
				break;
			if(start < location) {
				result++;
			}
		}
		return result;
	}
	
	public int getBonuses(int start, int end) {
		int result = 0;
		for( int i = 0; i<bonuses.length; i++) {
			if(end < bonuses[i] )
				break;
			if(start < bonuses[i] && bonusClaimed[i] == 0) {
				result++;
				bonusClaimed[i] = 1;
				if( i != bonuses.length - 1)
					nextUnclaimed = bonuses[i+1];				
			}
		}
		return result;
	}
	
	public String claimedToString() {
		String result = "";
		int i=0;
		for(int claimed : bonusClaimed) {
			result += claimed;
			if(i != bonusClaimed.length -1)
				result += ":";
			i++;
		}
		return result;
	}

	public void claimedFromString(String claimedString) {
		String[] ca = claimedString.split(":");
		bonusClaimed = new int[ca.length];
		int i = 0;
		for( String claimed : ca ) {
			bonusClaimed[i] = Integer.parseInt(claimed);
			i++;
		}
	}

	public TimeTrack cloneTrack() {
		TimeTrack clone = new TimeTrack();
		clone.id = this.id;
		clone.nextUnclaimed = this.nextUnclaimed;
		for(int i = 0; i<this.bonusClaimed.length; i++) {
			clone.bonusClaimed[i] = this.bonusClaimed[i];
		}
		return clone;
	}
}
