package com.patchwork.game.models.patches;

import java.util.LinkedList;

public class PatchList {
	private static final int STARTID = 33;
	private LinkedList<Patch> patches;
	private LinkedList<Patch> claimedPatches;
	public LinkedList<Patch> getPatches() {
		return patches;
	}

	public PatchList(Patch[] patches) {
		this.claimedPatches = new LinkedList<Patch>();
		this.patches = new LinkedList<Patch>();
		for( Patch p : patches )
			this.patches.add(p);
	}
	
	public Patch choose(int choice) {
		Patch result = getChoices()[choice];
		for(int i = 0; i<patches.size(); i++) {
			if(result.staticId == patches.get(i).staticId ) {
				result = patches.remove(i);
				result.wasChosen = 1;
				this.claimedPatches.add(result);
				for( int j = 0; j < i; j++)
					patches.add(patches.remove(0));
				break;
			}
		}
		return result;
	}
	
	public Patch[] getChoices() {
		Patch[] result = new Patch[3];
		for(int i = 0; i < 3; i++)
			result[i] = patches.get(i);
		return result;
	}
	
	public boolean start() {
		int i = 0;
		for(Patch p : patches) {
			if(p.staticId == STARTID)
				break;
			i++;
		}
		if(i == patches.size())
			return false;
		Patch p;
		for(int j = 0; j <= i; j++) {
			p = patches.pop();
			patches.add(p);
		}
		return true;
	}

	public Patch[] getUnclaimed() {
		return patches.toArray(new Patch[patches.size()]);
	}

	public Patch[] getClaimed() {
		return claimedPatches.toArray(new Patch[claimedPatches.size()]);
	}

	public PatchList clonePatches() {
		PatchList clone = new PatchList(new Patch[0]);
		for(Patch p : this.patches) {
			clone.patches.add(Patches.getPatch(p.staticId));
		}
		return clone;
	}
}
