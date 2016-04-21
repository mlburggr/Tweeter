package com.tweeter.app;

import java.util.Random;

public class BirdComputer extends Bird{
	
	/**
	 * Maximum length of a computer bird's random tweet.
	 */
	public static int TWEET_MAXLENGTH = 4;
	
	public BirdComputer(int origX, int origY){
		super(origX, origY);
		Random random = new Random();
		
		//Add random length tweet
		int length = (Math.abs(random.nextInt()) % (TWEET_MAXLENGTH-1)) +1;
		this.tweet = new Tweet();
		for (int i =0; i < length; i ++)
			tweet.add(Note.getNote( Math.abs( random.nextInt() ) % 6 ) );
		//
		
		this.health = 100;
		this.energy = 100;
	}
	
	// BirdComputer constructor for child bird spawned by dad and mom
	public BirdComputer(int origX, int origY, Bird dad, Bird mom){
		super(origX, origY);
		this.health = 100;
		this.energy = 100;
		Random random = new Random();
		
		this.tweet = new Tweet(dad.tweet, mom.tweet); 
		this.state = BirdState.NEWBORN;
		TweeterState.tweetQueue.addTweet(this.tweet, this.getPosX(), this.getPosY(), this);
		TweeterState.birdsToAdd.add(this);
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
	public void moveRandom(Map map) {
		Random random = new Random();
		int n = random.nextInt(4);
		if (n==0) { map.moveUp(this, this.getPosX(), this.getPosY()); }
		else if (n==1) { map.moveDown(this, this.getPosX(), this.getPosY()); }
		else if (n==2) { map.moveLeft(this, this.getPosX(), this.getPosY()); }
		else { map.moveRight(this, this.getPosX(), this.getPosY()); }
		
	}

	
	// Current prototype implementation of moving towards user bird
	public void moveToBird(Map map) {
		int x = this.movingTowards.getPosX();
		int y = this.movingTowards.getPosY();
		int distX = Math.abs(this.getPosX() - x);
		int distY = Math.abs(this.getPosY() - y);
		if (distX >= distY) {
			if (distX>1) {
				if (x > this.getPosX()) { map.moveRight(this, this.getPosX(), this.getPosY()); }
				else { map.moveLeft(this, this.getPosX(), this.getPosY()); }
			}
		}
		else {
			if (distY>1) {
				if (y > this.getPosY()) { map.moveDown(this, this.getPosX(), this.getPosY()); }
				else { map.moveUp(this, this.getPosX(), this.getPosY()); }
			}
		}
	}
	
	public void moveToCoord(Map map, int x, int y) {
		int distX = Math.abs(this.getPosX() - x);
		int distY = Math.abs(this.getPosY() - y);
		if (distX > distY) {
			if (distX>1) {
				if (x > this.getPosX()) { map.moveRight(this, this.getPosX(), this.getPosY()); }
				else { map.moveLeft(this, this.getPosX(), this.getPosY()); }
			}
		}
		else if (distX < distY) {
			if (distY>1) {
				if (y > this.getPosY()) { map.moveDown(this, this.getPosX(), this.getPosY()); }
				else { map.moveUp(this, this.getPosX(), this.getPosY()); }
			}
		}
		else {
			Random random = new Random();
			int guess = random.nextInt(2);
			if (guess==0) {
				if (x > this.getPosX()) { map.moveRight(this, this.getPosX(), this.getPosY()); }
				else { map.moveLeft(this, this.getPosX(), this.getPosY()); }
			} else {
				if (y > this.getPosY()) { map.moveDown(this, this.getPosX(), this.getPosY()); }
				else { map.moveUp(this, this.getPosX(), this.getPosY()); }
			}
		}
	}
	
	public void moveAwayCoord(Map map, int x, int y) {
		int distX = Math.abs(this.getPosX() - x);
		int distY = Math.abs(this.getPosY() - y);
		if (distX > distY) {
			if (distX>1) {
				if (x > this.getPosX()) { map.moveLeft(this, this.getPosX(), this.getPosY()); }
				else { map.moveRight(this, this.getPosX(), this.getPosY()); }
			}
		}
		else if (distX < distY) {
			if (distY>1) {
				if (y > this.getPosY()) { map.moveUp(this, this.getPosX(), this.getPosY()); }
				else { map.moveDown(this, this.getPosX(), this.getPosY()); }
			}
		}
		else {
			Random random = new Random();
			int guess = random.nextInt(2);
			if (guess==0) {
				if (x > this.getPosX()) { map.moveLeft(this, this.getPosX(), this.getPosY()); }
				else { map.moveRight(this, this.getPosX(), this.getPosY()); }
			} else {
				if (y > this.getPosY()) { map.moveUp(this, this.getPosX(), this.getPosY()); }
				else { map.moveDown(this, this.getPosX(), this.getPosY()); }
			}
		}
	}
	
}
