package com.ub.tfg.kukuicup.model;

import android.widget.ImageView;

public class Joke {
	private int id;
	private String title;
	private String text;
	private ImageView img;
	
	public Joke(){
		
	}

	public Joke(int id, String title, String text) {
		this.id = id;
		this.title = title;
		this.text = text;
	}

	public Joke(int id, String title, ImageView img) {
		this.id = id;
		this.title = title;
		this.img = img;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public ImageView getImg() {
		return img;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setImg(ImageView img) {
		this.img = img;
	}

}
