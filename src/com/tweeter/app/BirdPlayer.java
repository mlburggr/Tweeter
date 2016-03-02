package com.tweeter.app;

public class BirdPlayer extends Bird{
	
	public BirdPlayer(int origX, int origY){
		super(origX, origY);
	}
	
	public BirdPlayer(int origX, int origY, boolean userBird){
		super(origX, origY);
		if(userBird) { this.mode = 2; }
	}
	
	public void setMovingTowards(Bird b){
		this.movingTowards = b; 
	}
	
	public boolean isUserBird(){
		return mode == 2;
	}
	
}
