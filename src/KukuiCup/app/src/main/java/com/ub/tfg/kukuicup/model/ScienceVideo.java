package com.ub.tfg.kukuicup.model;

public class ScienceVideo extends Activity {

	private String uRL;
	private TestForm test;
	
	public ScienceVideo() {
	}

	public ScienceVideo(String uRL, TestForm test) {
		this.uRL = uRL;
		this.test = test;
	}

	public String getURL() {
		return uRL;
	}

	public void setURL(String uRL) {
		this.uRL = uRL;
	}

	public TestForm getTest() {
		return test;
	}

	public void setTest(TestForm test) {
		this.test = test;
	}
	

}
