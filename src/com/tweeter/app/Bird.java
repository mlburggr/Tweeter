package com.tweeter.app;

public abstract class Bird {
	
	protected int health;
	protected int energy;
	protected int id;
	protected Tweet tweet;
	protected int mode; 	// private to protected for abstraction
	protected int posX;
	protected int posY;
	protected Bird movingTowards;	// private to protected for abstraction
	private BirdState state;
	private int stateTime;

	public Bird(int origX, int origY){
		this.posX = origX;
		this.posY = origY;
		this.setBirdState(BirdState.NORMAL);
		this.setStateTime(0);
	}
	
	public void tweet(){
		
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getEnergy(){
		return energy;
	}
	
	public int getId(){
		return id;
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public int getMode(){
		return mode;
	}
	
	public Tweet getTweet(){
		return tweet;
	}
	
	public Bird getMovingTowards(){
		return movingTowards;
	}
	
	public void setHealth(int h){
		this.health = h;
	}
	
	public void setEnergy(int e){
		this.energy = e;
	}
	
	public void setId(int i){
		this.id = i;
	}
	
	public void setPosX(int x){
		this.posX = x;
	}
	
	public void setPosY(int y){
		this.posY = y;
	}
	
	public void setMode(int m){
		this.mode = m;
	}
	
	public void setTweet(Tweet t){
		this.tweet = t;
	}
	
	abstract void setMovingTowards(Bird b);
	
	abstract boolean isUserBird();

	public BirdState getBirdState() {
		return state;
	}

	public void setBirdState(BirdState state) {
		this.state = state;
	}

	public int getStateTime() {
		return stateTime;
	}

	public void setStateTime(int stateTime) {
		this.stateTime = stateTime;
	}
	
}
