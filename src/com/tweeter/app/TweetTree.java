package com.tweeter.app;

//TODO Actually make this a heap...

public class TweetTree {
	/**
	 * "center" node of tweet tree
	 */
	private TweetNode root;
	
	public TweetTree() {
		root = null;
	}
	
	/**
	 * Method to add bird's tweet to global tweet tree.
	 * 
	 * @param tweet : tweet to be added
	 * @param x : x coordinate of bird
	 * @param y : y coordinate of bird
	 */
	public void addTweet(Tweet tweet, int x, int y){
		if (root == null)
			root = new TweetNode(tweet, x, y);
		else {
			root.addTweet( tweet, x, y ); }
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
		TweetNode closest = null;
		
		
		
		return closest;
	}
}

/**
 * Class to hold a quadrant of coordinates
 * 
 * @author nick
 */
class TweetNode {
	public int x0;
	public int y0;
	public Tweet tweet;
	protected TweetNode NE, NW, SE, SW;
	
	/**
	 * Constructor of a node for Tweet quad tree.  
	 * 
	 * @param t : tweet to be put in tree
	 * @param x : x coordinate of tweet
	 * @param y : y coordinate of tweet
	 */
	public TweetNode(Tweet t, int x, int y){
		x0 = x;
		y0 = y;
		tweet = t;
		SE = null;
		SW = null;
		NE = null;
		NW = null;
	}
	
	public boolean addTweet(Tweet t, int x, int y){
		if (x - x0 > 0){
			// Q1
			if (y - y0 > 0) {
				if (SE == null)
					SE = new TweetNode(t, x, y);
				else
					SE.addTweet(t, x, y); }
			// Q3
			else {
				if (NE == null)
					NE = new TweetNode(t, x, y);
				else
					NE.addTweet(t, x, y); }}
		else {
			// Q2
			if (y - y0 > 0){
				if (SW == null)
					SE = new TweetNode(t, x, y);
				else
					SW.addTweet(t, x, y); }
			// Q4
			else {
				if (NW == null)
					NW = new TweetNode(t, x, y);
				else
					NW.addTweet(t, x, y); }
		}
			
		return true;
	}
	
	//Getters and setters
	public TweetNode getNE() {
		return NE;}

	public void setNE(TweetNode nE) {
		NE = nE;}

	public TweetNode getNW() {
		return NW;}

	public void setNW(TweetNode nW) {
		NW = nW;}

	public TweetNode getSE() {
		return SE;}

	public void setSE(TweetNode sE) {
		SE = sE;}

	public TweetNode getSW() {
		return SW;}

	public void setSW(TweetNode sW) {
		SW = sW;}
}
