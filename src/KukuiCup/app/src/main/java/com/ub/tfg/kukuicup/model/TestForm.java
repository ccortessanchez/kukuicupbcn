package com.ub.tfg.kukuicup.model;

import java.util.ArrayList;

public class TestForm extends Activity{

	private int numQuestions;
	private ArrayList<String> questionList;
	private ArrayList<String> answerList;
	private Joke joke;
	
	
	public TestForm() {
		questionList = new ArrayList<String>();
		answerList = new ArrayList<String>();
	}

	public TestForm(int numQuestions, ArrayList<String> questionList,
			ArrayList<String> aswerList) {
		this.numQuestions = numQuestions;
		this.questionList = questionList;
		this.answerList = aswerList;
	}

	public int getNumQuestions() {
		return numQuestions;
	}

	public ArrayList<String> getQuestionList() {
		return questionList;
	}

	public ArrayList<String> getAnswerList() {
		return answerList;
	}

	public void setNumQuestions(int numQuestions) {
		this.numQuestions = numQuestions;
	}

	public void setQuestionList(ArrayList<String> questionList) {
		this.questionList = questionList;
	}

	public void setAnswerList(ArrayList<String> answerList) {
		this.answerList = answerList;
	}

	public Joke getJoke() {
		return joke;
	}

	public void setJoke(Joke joke) {
		this.joke = joke;
	}
	
	
}
