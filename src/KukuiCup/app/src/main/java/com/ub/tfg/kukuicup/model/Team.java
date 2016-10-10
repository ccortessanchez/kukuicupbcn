package com.ub.tfg.kukuicup.model;

import java.util.ArrayList;

import android.widget.ImageView;

public class Team {
	
	private int id;
	private String name;
	private ArrayList<User> userList;
	private int points;
	private int classification;
	private ImageView teamImg;
	private ArrayList<Badge> badgeList;
	private static Team instance = null;
	
	
	public Team() {
		badgeList = new ArrayList<Badge>();
		userList = new ArrayList<User>();
	}



	public Team(int id, String name) {
		this.id = id;
		this.name = name;
		userList = new ArrayList<User>();
        badgeList = new ArrayList<Badge>();
	}



	public Team(int id, String name, ArrayList<User> userList, int points,
			int classification, ImageView teamImg) {
		this.id = id;
		this.name = name;
		this.userList = userList;
		this.points = points;
		this.classification = classification;
		this.teamImg = teamImg;
        badgeList = new ArrayList<Badge>();
	}

	public static Team getInstance() {
		if (instance==null) {
			instance = new Team();
		}
		return instance;
	}

	public int getId() {
		return id;
	}



	public String getName() {
		return name;
	}



	public ArrayList<User> getUserList() {
		return userList;
	}



	public int getPoints() {
		return points;
	}



	public int getClassification() {
		return classification;
	}



	public ImageView getTeamImg() {
		return teamImg;
	}



	public void setId(int id) {
		this.id = id;
	}



	public void setName(String name) {
		this.name = name;
	}



	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}



	public void setPoints(int points) {
		this.points = points;
	}



	public void setClassification(int classification) {
		this.classification = classification;
	}



	public void setTeamImg(ImageView teamImg) {
		this.teamImg = teamImg;
	}

	public Badge getBadgeByName(String name) {
		if(!this.badgeList.isEmpty()) {
		    for (int i=0; i<badgeList.size();i++) {
			    if(badgeList.get(i).getName().equals(name)) {
				    return badgeList.get(i);
			    }
		    }
		}
		return null;
	}

	public ArrayList<Badge> getListBadges() {
		return badgeList;
	}

	public void addBadge (Badge badge) {
		this.badgeList.add(badge);
	}
	

}
