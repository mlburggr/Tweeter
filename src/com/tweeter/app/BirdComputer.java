package com.tweeter.app;

public class BirdComputer implements Bird{

	private int posX;
	private int posY;

	public BirdComputer(int origX, int origY){
		this.posX = origX;
		this.posY = origY;
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}

	public void setPosX(int x){
		this.posX = x;
	}
	
	public void setPosY(int y){
		this.posY = y;
	}

	@Override
	public boolean isUserBird() {
		// TODO Auto-generated method stub
		return false;
	}

}
