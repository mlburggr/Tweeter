package com.tweeter.app.birds;

import org.newdawn.slick.Color;
import com.tweeter.app.globals.Global;
import com.tweeter.app.sound.Tweet;
import com.tweeter.app.sound.TweetSynth;

/**
 * @author nick
 *
 */
public abstract class Bird {
	
	/**
	 * How many ticks a bird waits before gaining energy
	 */
	public static final int ENERGY_COOLDOWN = 100;

	/**
	 * The maximum length of a bird's tweet
	 */
	public static final int TWEET_MAXLENGTH = 4;
	
	/**
	 * How much energy a bird recovers after one cooldown
	 */
	public static int ENERGY_RECOVERY = 20;
	
	/**
	 * How much energy a bird action costs
	 */
	public static int ENERGY_COST = 5;
		
	protected int health;
	protected int energy;
	
	protected int posX;
	protected int posY;
	protected BirdMood mood;

	public final int DAMAGE = 50;
	public TweetSynth tweetSynth;
	protected Tweet tweet;
	
	public Bird(int origX, int origY){
		this.posX = origX;
		this.posY = origY;
	}
	
	/*
	 * Send tweet to sound and queue
	 */
	protected void tweet(){
		double xposition = (2.0 * posX / (double) Global.map.getSizeX()) - 1.0;
		
		tweetSynth.queueTweet(tweet, xposition);
		
		Global.tweetQueue.addTweet(tweet, this.posX, this.posY);
	}
	
	/*
	 * empty the action stack. (Ends the bird thread)
	 */
	protected void die(){
		Global.tweetPlyr.remove( this.tweetSynth );
		Global.map.removeBird(posX, posY);
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getEnergy(){
		return energy;
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public Tweet getTweet(){
		return tweet;
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
	
	public void setTweet(Tweet t){
		this.tweet = t;
	}
	
	public synchronized void setMood(BirdMood mood) {
		this.mood = mood;
	}

	public synchronized BirdMood getMood() {
		return mood;
	}	
}
