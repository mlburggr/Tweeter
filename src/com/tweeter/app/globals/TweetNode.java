package com.tweeter.app.globals;

import com.tweeter.app.sound.Tweet;

/**
 * Class to hold a tweet and its coordinates
 * 
 * @author nick
 */
public class TweetNode {
	public int x0;
	public int y0;
	public Tweet tweet;

	/**
	 * Constructor, I mean, C'mon.  
	 * 
	 * @param t : tweet to be put in tree
	 * @param x : x coordinate of tweet
	 * @param y : y coordinate of tweet
	 */
	public TweetNode(Tweet t, int x, int y){
		x0 = x;
		y0 = y;
		tweet = t;
	}

	/**
	 * You know... distance...
	 * 
	 * @param x : x coordinate of whatever you know what's 
	 * 				going on
	 * @param y : y coordinate of seriously why are you 
	 * 				reading this documentation anyway
	 * @return 	distance of this TweetNode to the xy-coordinate 
	 * 			of the parameters.
	 */
	public double distance(int x, int y){
		return Math.hypot(x-x0, y-y0);
	}
}