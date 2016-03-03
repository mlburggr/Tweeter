package com.tweeter.app;

import java.util.Random;

public class BirdComputer extends Bird{

	public BirdComputer(int origX, int origY){
		super(origX, origY);
	}

	@Override
	void setMovingTowards(Bird b) {
		// TODO Auto-generated method stub
		
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
