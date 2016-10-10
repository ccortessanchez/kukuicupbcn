package com.ub.tfg.kukuicup.model;

import java.util.ArrayList;
import java.util.Date;

public class EnergyTournament {
	
	private String name;
	private int id;
	private Date initData;
    private Date finishData;
	private int duration;
	private ArrayList<Level> levelList;
	private ArrayList<Team> teamList;
	private ArrayList<User> userList;
	
	public EnergyTournament() {
		levelList = new ArrayList<Level>();
		teamList = new ArrayList<Team>();
		userList = new ArrayList<User>();
	}
	
	

	public EnergyTournament(String name, int id) {
		this.name = name;
		this.id = id;
		levelList = new ArrayList<Level>();
		teamList = new ArrayList<Team>();
		userList = new ArrayList<User>();		
	}

	
	public EnergyTournament(String name, int id, Date initData, Date finishData, int duration, ArrayList<Level> levelList,
                            ArrayList<Team> teamList, ArrayList<User> userList) {
		this.name = name;
		this.id = id;
        this.initData = initData;
        this.finishData = finishData;
        this.duration = duration;
        this.levelList = levelList;
		this.teamList = teamList;
		this.userList = userList;
	}



	public String getName() {
		return name;
	}



	public int getId() {
		return id;
	}



	public ArrayList<Level> getLevelList() {
		return levelList;
	}



	public ArrayList<Team> getTeamList() {
		return teamList;
	}



	public ArrayList<User> getUserList() {
		return userList;
	}



	public void setName(String name) {
		this.name = name;
	}



	public void setId(int id) {
		this.id = id;
	}



	public void setLevelList(ArrayList<Level> levelList) {
		this.levelList = levelList;
	}



	public void setTeamList(ArrayList<Team> teamList) {
		this.teamList = teamList;
	}



	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}


    public Date getInitData() {
        return initData;
    }

    public void setInitData(Date initData) {
        this.initData = initData;
    }

    public Date getFinishData() {
        return finishData;
    }

    public void setFinishData(Date finishData) {
        this.finishData = finishData;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
