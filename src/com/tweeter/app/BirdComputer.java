package com.tweeter.app;

import java.util.Random;

public class BirdComputer extends Bird{

	private int health;
	private int energy;

	public BirdComputer(int origX, int origY){
		super(origX, origY);
		this.health = 100;
		this.energy = 100;
				
	}

	@Override
	void setMovingTowards(Bird b) {
		// TODO Auto-generated method stub
		
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
	
	public void randomMove(Map map) {
		Random random = new Random();
		int n = random.nextInt(4);
		if (n==0) { map.moveUp(this, this.getPosX(), this.getPosY()); }
		else if (n==1) { map.moveDown(this, this.getPosX(), this.getPosY()); }
		else if (n==2) { map.moveLeft(this, this.getPosX(), this.getPosY()); }
		else { map.moveRight(this, this.getPosX(), this.getPosY()); }
	}
	
}
