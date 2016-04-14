package com.tweeter.app;

/**
 * Class to store all tweeted tweets on the gameboard.
 * Basically it just truncates the list if it's about 
 * to overflow.  
 * 
 * TODO probably should scale PRIORITY with the bird population level... 
 * 
 * @author nick
 *
 */
public class TweetQueue {
	/**
	 * Initial level of priority when tweet is added.
	 */
	public static final int PRIORITY = 10;
	
	
	/**
	 * list of priorities of tweets.
	 */
	private int [] priorities;
	
	/**
	 * list of tweets.
	 */
	private TweetNode [] tweets;
	
	/**
	 * Size of queue
	 */
	private int size;
	
	/**
	 * Number of tweets in queue
	 */
	private int elems;
	
	/**
	 * points to the current tweet in the queue
	 */
	private int index;
	
	/**
	 * initialize the queue
	 * @param birdPopulation
	 */
	public TweetQueue(int birdPopulation) {
		elems = 0;
		index = 0;
		
		size = birdPopulation * 2;
		
		priorities = new int [size];
		tweets = new TweetNode[size];
	}
	
	/**
	 * Method to add bird's tweet to global tweet tree.
	 * 
	 * @param tweet : tweet to be added
	 * @param x : x coordinate of bird
	 * @param y : y coordinate of bird
	 */
	public void addTweet(Tweet tweet, int x, int y){
		if (elems == size){
			int beforeIndex = (index - 1) % elems;
			priorities[beforeIndex] = PRIORITY;
			tweets[beforeIndex] = new TweetNode(tweet, x, y);
		} else {
			priorities[++elems] = PRIORITY;
			tweets[elems] = new TweetNode(tweet, x, y);
		}
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
		double distance = Double.MAX_VALUE;
		
		for (int i = 0; i < elems; i++){
			if (--priorities[index] == 0){
				priorities[index] = priorities[--elems];
				tweets[index] = tweets[elems];}
			else {	
			        double newDistance = tweets[index].distance(x, y);
			        if (newDistance > 1){
				        if (newDistance < distance){
				        	closest = tweets[index];
				        	distance = newDistance;
				        }
			        }
			        index = (index + 1) % elems;
			}
		}
		return closest;
	}
}

/**
 * Class to hold a tweet and its coordinates
 * 
 * @author nick
 */
class TweetNode {
	public int x0;
	public int y0;
	public Tweet tweet;
	
	/**
	 * Constructor,I mean, C'mon.  
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
