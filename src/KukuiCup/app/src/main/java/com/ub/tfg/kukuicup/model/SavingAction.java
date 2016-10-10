package com.ub.tfg.kukuicup.model;

public class SavingAction extends Activity {
	
	private int points;

	public SavingAction(int id, String name, String description, int points) {
		super(id, name, description);
		this.points = points;
	}

	public SavingAction() {
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
