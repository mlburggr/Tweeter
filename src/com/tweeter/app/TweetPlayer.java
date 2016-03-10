package com.tweeter.app;

import com.softsynth.jsyn.*;
import com.tweeter.app.Note;
import com.tweeter.app.Tweet;
import com.tweeter.app.TweetSynth;

public class TweetPlayer {

	/**
	 * Frequency step of one semitone in hz. (2 ^ 1/12)
	 */
	public static final double SEMITONE = 1.059463094;	
	
	/**
	 * Base frequency (A2)
	 */
	public static final double BASE = 110.0;	
	
	public TweetPlayer() {
		
	}
	
	
	public void playTweet(Tweet t){
		Note [] tweet = t.toArray( new Note [0]);
		
		double [] tweetFreqEnvelope = new double[2 * tweet.length]; 
		
		for (int i =0 ,j = 0; i < tweet.length * 2; i += 2, j++){
			tweetFreqEnvelope[i] = BASE + (tweet[j].ordinal() * SEMITONE);
			tweetFreqEnvelope[i+1] = Note.DURATION; }
		
	}
}
