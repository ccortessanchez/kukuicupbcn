package com.ub.tfg.kukuicup.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.widget.ImageView;

public class Player extends User implements Serializable{
	private static Player instance = null;
	private int points;
	private int ranking;
	private ArrayList<Badge> badgeList;
	private int level;
	private ImageView avatar;

	public Player() {
		badgeList = new ArrayList<Badge>();
	}

	public Player(int id, String name, String passwd) {
		super(id, name, passwd);
		badgeList = new ArrayList<Badge>();
	}

	public static Player getInstance() {
		if (instance==null) {
			instance = new Player();
		}
		return instance;
	}

	public int getPoints() {
		return points;
	}

	public int getRanking() {
		return ranking;
	}

	public ArrayList<Badge> getListBadges() {
		return badgeList;
	}

	public int getLevel() {
		return level;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public void setListBadges(ArrayList<Badge> listBadges) {
		this.badgeList = listBadges;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ImageView getAvatar() {
		return avatar;
	}

	public void setAvatar(ImageView avatar) {
		this.avatar = avatar;
	}

	public Badge getBadgeByName(String name) {
		if(!badgeList.isEmpty()) {
			for (int i = 0; i < badgeList.size(); i++) {
				if (badgeList.get(i).getName().equals(name)) {
					return badgeList.get(i);
				}
			}
		}
		return null;
	}

	public void addBadge (Badge badge) {
		this.badgeList.add(badge);
	}

}
