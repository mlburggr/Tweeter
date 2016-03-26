package com.tweeter.app;

public class BirdPlayer extends Bird{
	
	private int health;
	private int energy;

	public BirdPlayer(int origX, int origY){
		super(origX, origY);
		this.health = 100;
		this.energy = 100;
	}
	
	public BirdPlayer(int origX, int origY, boolean userBird){
		super(origX, origY);
		if(userBird) { this.mode = 2; }
		this.health = 100;
		this.energy = 100;
	}
	
	public BirdPlayer(int origX, int origY, boolean userBird, int energyLimit){
		super(origX, origY);
		if(userBird) { this.mode = 2; }
		this.health = 100;
		this.energy = energyLimit;
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
	public void setMovingTowards(Bird b){
		this.movingTowards = b; 
	}
	
	public boolean isUserBird(){
		return mode == 2;
	}
	
	public void tweet(){
		//TODO: make this
	}
	
}
