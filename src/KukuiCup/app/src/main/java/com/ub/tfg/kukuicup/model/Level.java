package com.ub.tfg.kukuicup.model;

import java.util.ArrayList;

public class Level {
	
	private int number;
	private int state;
	private ArrayList<Activity> activityList;
	
	public Level() {
	}

	public Level(int number, int state, ArrayList<Activity> activityList) {
		this.number = number;
		this.state = state;
		this.activityList = activityList;
	}

	public int getNumber() {
		return number;
	}

	public int getState() {
		return state;
	}

	public ArrayList<Activity> getActivityList() {
		return activityList;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setActivityList(ArrayList<Activity> activityList) {
		this.activityList = activityList;
	}
	
	

}
