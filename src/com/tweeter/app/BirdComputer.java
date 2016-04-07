package com.tweeter.app;

import java.util.Random;

public class BirdComputer extends Bird{
	
	/**
	 * Maximum length of a computer bird's random tweet.
	 */
	private static int TWEET_MAXLENGTH;
	
	public BirdComputer(int origX, int origY){
		super(origX, origY);
		Random random = new Random(42);
		
		//Add random length tweet
		int length = random.nextInt() % TWEET_MAXLENGTH;
		this.tweet = new Tweet();
		for (int i =0; i < length; i ++)
			tweet.add(Note.getNote( random.nextInt() % 6 ) );
		//
		
		this.health = 100;
		this.energy = 100;
	}

	@Override
	void setMovingTowards(Bird b) {
		this.movingTowards = b;
		
	}
	
	@Override
	public int getHealth(){
		return health;
	}
	
	@Override
	public int getEnergy(){
		return energy;
	}
	
	@Override
	public void setEnergy(int en){
		this.energy = en;
	}
	
	@Override
	public void setHealth(int hp){
		this.health = hp;
	}

	@Override
	boolean isUserBird() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Prototype implementation of NPC birds normal movements
	public void randomMove(Map map) {
		Random random = new Random();
		int n = random.nextInt(4);
		if (n==0) { map.moveUp(this, this.getPosX(), this.getPosY()); }
		else if (n==1) { map.moveDown(this, this.getPosX(), this.getPosY()); }
		else if (n==2) { map.moveLeft(this, this.getPosX(), this.getPosY()); }
		else { map.moveRight(this, this.getPosX(), this.getPosY()); }
	}
	
	// Current prototype implementation of moving towards user bird
	public void moveTowards(Map map) {
		int x = this.movingTowards.getPosX();
		int y = this.movingTowards.getPosY();
		int distX = Math.abs(this.getPosX() - x);
		int distY = Math.abs(this.getPosY() - y);
		if (distX >= distY) {
			if (distX!=1) {
				if (x > this.getPosX()) { map.moveRight(this, this.getPosX(), this.getPosY()); }
				else { map.moveLeft(this, this.getPosX(), this.getPosY()); }
			}
		}
		else {
			if (distY!=1) {
				if (y > this.getPosY()) { map.moveDown(this, this.getPosX(), this.getPosY()); }
				else { map.moveUp(this, this.getPosX(), this.getPosY()); }
			}
		}
	}
}
