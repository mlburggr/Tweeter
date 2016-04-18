package com.tweeter.app;

import java.util.Random;

import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.VariableRateMonoReader;

public abstract class Bird {
	
	protected int health;
	protected int energy;
	protected final int id;
	protected Tweet tweet;
	protected int mode; 	// private to protected for abstraction
	protected int posX;
	protected int posY;
	protected Bird movingTowards;	// private to protected for abstraction
	protected BirdState state;
	protected BirdMood mood;
	protected int stateTime;

	public Bird(int origX, int origY){
		Random r = new Random(42);
		this.id = r.nextInt();
		this.posX = origX;
		this.posY = origY;
		this.state = BirdState.DEFAULT;
		this.stateTime = 0;
		this.mood = BirdMood.NEUTRAL;
	}
	
	public void tweet(VariableRateMonoReader tweetFreqEnv, TweetQueue tweetQueue){
			Note [] tweetArr = tweet.toArray( new Note [0]);
			
			
			double [] tweetFreqDat = new double[4 * (tweetArr.length+1)]; 
			
			// Translate tweet
			for (int i =0 ,j = 0; i < tweetArr.length * 4; i += 4, j++){
				tweetFreqDat[i] = 0.0 ;
				tweetFreqDat[i+1] = Note.BASE * (Math.pow(Note.SEMITONE, tweetArr[j].semi));
				tweetFreqDat[i+2] = Note.DURATION; 
				tweetFreqDat[i+3] = tweetFreqDat[i+1];
				System.out.printf("%fhz for %fsecs\n", tweetFreqDat[i+1], tweetFreqDat[i+2]);}
			// end it son
			tweetFreqDat[tweetFreqDat.length - 4] = 0;
			tweetFreqDat[tweetFreqDat.length - 3] = 0;
			tweetFreqDat[tweetFreqDat.length - 2] = Note.DURATION * tweetArr.length;
			tweetFreqDat[tweetFreqDat.length - 1] = 0;
			
			//load tweet data to envelope
			SegmentedEnvelope tweetFreqEnvDat = new SegmentedEnvelope(tweetFreqDat);
			
			//Start playing tweet
			tweetFreqEnv.dataQueue.clear();
			tweetFreqEnv.dataQueue.queueLoop(tweetFreqEnvDat, 0, tweetFreqEnvDat.getNumFrames());
			
			tweetQueue.addTweet(tweet, this.posX, this.posY, this);
			
			System.out.println("Reached!");	
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
	
	public void mate(Bird partner, Map map) {
		int xMin, yMin, xMax, yMax;
		if (this.getPosX() - 1 > 0) xMin = this.getPosX() - 1; else xMin = 0;
		if (this.getPosY() - 1 > 0) yMin = this.getPosY() - 1; else yMin = 0;
		if (this.getPosX() + 1 >= map.sizeX) xMax = this.getPosX() - 1; else xMax = map.sizeX-1;
		if (this.getPosY() + 1 >= map.sizeY) yMax = this.getPosY() - 1; else yMax = map.sizeY-1;
		
		for (int x = xMin; x <= xMax; x++) {
			for (int y = yMin; y <= yMax; y++) {
				Cell c = map.getCellAt(x, y);
				if (!c.hasBird()) {
						BirdComputer child = new BirdComputer(x,y, this, partner); 
						map.addBird(child);
						break;
				}
			}
		} // end of nested for loop that spawns bird
		
	}
	
}
