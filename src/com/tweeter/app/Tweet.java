package com.tweeter.app;
import java.util.LinkedList;

import com.tweeter.app.Note;
import java.lang.Math.*;

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
	
	public static double compare(Tweet heard, Tweet tweeted){
		// LevenshteinDistance from wikipedia
		int hsize = heard.size();
		int tsize = tweeted.size();

		if (hsize == 0) return tsize;
		if (tsize == 0) return hsize;

		// to arrays
		final Note [] h = heard.toArray(new Note[0]);
		final Note [] t = heard.toArray(new Note[0]);


		// create two work vectors of integer distances
		int[] v0 = new int[tsize + 1];
		int[] v1 = new int[tsize + 1];

		// initialize v0 (the previous row of distances)
		// this row is A[0][i]: edit distance for an empty s
		// the distance is just the number of characters to delete from t
		for (int i = 0; i < v0.length; i++)
			v0[i] = i;

		for (int i = 0; i < hsize; i++)
		{
			// calculate v1 (current row distances) from the previous row v0

			// first element of v1 is A[i+1][0]
			//   edit distance is delete (i+1) chars from s to match empty t
			v1[0] = i + 1;

			// use formula to fill in the rest of the row
			for (int j = 0; j < tsize; j++)
			{
				int cost = Note.getInterval(h[i], t[j]);
				v1[j + 1] = Math.min(v1[j] + h[i].ordinal(), 
						Math.min(v0[j + 1] + t[j].ordinal(), v0[j] + cost) );
			}

			// copy v1 (current row) to v0 (previous row) for next iteration
			for (int j = 0; j < v0.length; j++)
				v0[j] = v1[j];
		}

		return Math.log(v1[tsize]/(Note.SUM * hsize));
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