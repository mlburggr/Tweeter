package com.tweeter.app;
import java.util.LinkedList;
import com.tweeter.app.Note;


/**
 * 
 * @author nick
 *
 */
public class Tweet extends LinkedList<Note>{
	
	/**
	 * @author nick
	 * 
	 * Tweet: initial note constructor
	 * 
	 * @param c -The first note of the tweet
	 */
	public Tweet(char c){
		super();
		add(Note.getNote(c));
	}
	
	/**
	 * @author nick
	 * 
	 * Tweet: full char array constructor
	 * 
	 * @param cs 
	 */
	public Tweet(char cs[]){
		super();
		for(int i = 1; i < cs.length; i++){
			add(Note.getNote(cs[i]));
		}
	}
	
	/**
	 * 
	 * @param listener
	 * @param tweeter
	 * @return double value "similarity index" 	
	 * 					negative -> not similar
	 * 					positive -> similar
	 */
	
	public static double compare(Tweet listener, Tweet tweeter){
		return 0.0;
	}
	
	/**
	 * Testing for the tweet and tweet synth classes
	 * @param args
	 */
	public static void main(String[] args){
		char [] tweetstring = {'a','e','q','d','s','w'};
		Tweet testtweet = new Tweet(tweetstring);
		
		//TweetSynth.
	}
}