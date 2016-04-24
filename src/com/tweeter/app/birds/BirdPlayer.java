package com.tweeter.app.birds;

import com.tweeter.app.globals.Global;
public class BirdPlayer extends Bird{
	
	/**
	 * player bird is always in the player mood
	 */
	final BirdMood mood;
	
	public BirdPlayer(int origX, int origY) {
		super(origX, origY);
		this.mood = BirdMood.PLAYER; 
	}
	
	/**
	 * Send tweet to sound and queue
	 */
	public void tweet(){
		double xposition = (2.0 * posX / (double) Global.map.getSizeX()) - 1.0;
		
		tweetSynth.queueTweet(tweet, xposition);
		
		Global.tweetQueue.addTweet(tweet, this.posX, this.posY);
	}

	
	/**
	 * Add energy increment to this bird's energy
	 */
	public void increaseEnergy() {
		if (this.energy + Bird.ENERGY_RECOVERY <= 100)
			this.energy += Bird.ENERGY_RECOVERY;
		else
			this.energy = 100;
	}
}

