package com.tweeter.app;

public class TweetTree {

	public TweetTree() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Method to add bird's tweet to global tweet tree.
	 * 
	 * @param tweet : tweet to be added
	 * @param x : x coordinate of bird
	 * @param y : y coordinate of bird
	 */
	public void addTweet(Tweet tweet, int x, int y){
		
	}
	
	/**
	 * Method to find a tweet to learn from. 
	 * --Calvin, this returns a @code TweetNode
	 * but the Tweet and x,y coordinates are 
	 * public in that object so just use result.tweet
	 * and result.x, result.y to get the relevant information
	 * 
	 * @param x : the x coordinate of listening bird
	 * @param y : the y coordinate of listening bird
	 * @return closest tweet to the listening bird
	 */
	public TweetNode listen(int x, int y){
		return new TweetNode();
	}
}

/**
 * Class to hold a quadrant of coordinates
 * 
 * @author nick
 */
class TweetNode {
	public int x;
	public int y;
	public Tweet tweet;
	private TweetNode q1, q2, q3, q4;
	
	
	//Getters and setters
	public TweetNode getQ1() {
		return q1;
	}
	public void setQ1(TweetNode q1) {
		this.q1 = q1;
	}
	public TweetNode getQ2() {
		return q2;
	}
	public void setQ2(TweetNode q2) {
		this.q2 = q2;
	}
	public TweetNode getQ3() {
		return q3;
	}
	public void setQ3(TweetNode q3) {
		this.q3 = q3;
	}
	public TweetNode getQ4() {
		return q4;
	}
	public void setQ4(TweetNode q4) {
		this.q4 = q4;
	}
}
