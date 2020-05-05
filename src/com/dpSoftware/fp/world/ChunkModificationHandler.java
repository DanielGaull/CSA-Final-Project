package com.dpSoftware.fp.world;

import java.util.ArrayList;

public class ChunkModificationHandler {

	private ArrayList<ChunkModification> modifications;
	
	public ChunkModificationHandler() {
		modifications = new ArrayList<>();
	}

	public boolean hadModification(int x, int y) {
		for (int i = 0; i < modifications.size(); i++) {
			if (modifications.get(i).getX() == x &&
					modifications.get(i).getY() == y) {
				return true;
			}
		}
		return false;
	}
	public ChunkModification getModification(int x, int y) {
		for (int i = 0; i < modifications.size(); i++) {
			if (modifications.get(i).getX() == x &&
					modifications.get(i).getY() == y) {
				return modifications.get(i);
			}
		}
		return null;
	}
	public ArrayList<ChunkModification> getModifications() {
		return modifications;
	}
	public void addModification(ChunkModification modification) {
		modifications.add(modification);
	}
	public void deleteModification(int x, int y) {
		for (int i = 0; i < modifications.size(); i++) {
			if (modifications.get(i).getX() == x &&
					modifications.get(i).getY() == y) {
				modifications.remove(i);
				return;
			}
		}
	}
	
}
