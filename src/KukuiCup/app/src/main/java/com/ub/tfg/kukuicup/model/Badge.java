package com.ub.tfg.kukuicup.model;

import android.widget.ImageView;

public class Badge {
	private int id;
	private String name;
	private ImageView image;
	
	public Badge() {
		super();
	}

	public Badge(int id, String name, ImageView image) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ImageView getImage() {
		return image;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}

}
