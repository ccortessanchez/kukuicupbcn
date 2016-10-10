package com.ub.tfg.kukuicup.model;

public class User {
	private int id;
	private String name;
	private String passwd;
	
	public User() {
	}

	public User(int id, String name, String passwd) {
		this.id = id;
		this.name = name;
		this.passwd = passwd;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	
	
	
}
