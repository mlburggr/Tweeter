package com.tweeter.app.sound;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import com.tweeter.app.birds.BirdComputer;
import com.tweeter.app.globals.TweetQueue;
import com.tweeter.app.sound.Note;

import java.lang.Math.*;

/**
 * 
 * @author nick
 *
 */
public class Tweet extends LinkedList<Note>{
	
	/**
	 * I don't know what the fuck this is for.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * number of set (immutable) notes in the tweet.
	 */
	private int setNotes;
	
	public int getSetNotes() {
		return setNotes;
	}

	public void setSetNotes(int setNotes) {
		this.setNotes = setNotes;
	}

	public Tweet(){
		super();
	}
	
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
		for(int i = 0; i < cs.length; i++){
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
		final int OMIT_COST = Note.getNote(5).semi;
		int hsize = heard.size();
		int tsize = tweeted.size();

		if (hsize == 0) return tsize;
		if (tsize == 0) return hsize;

		// to arrays
		final Note [] h = heard.toArray(new Note[0]);
		final Note [] t = tweeted.toArray(new Note[0]);


		// create two work vectors of integer distances
		int[] v0 = new int[tsize + 1];
		int[] v1 = new int[tsize + 1];

		// initialize v0 (the previous row of distances)
		// this row is A[0][i]: edit distance for an empty s
		// the distance is just the number of characters to delete from t
		for (int i = 0; i < v0.length; i++)
			v0[i] = i * OMIT_COST;

		for (int i = 0; i < hsize; i++)
		{
			// calculate v1 (current row distances) from the previous row v0

			// first element of v1 is A[i+1][0]
			//   edit distance is delete (i+1) chars from s to match empty t
			v1[0] = (i + 1) * OMIT_COST;

			// use formula to fill in the rest of the row
			for (int j = 0; j < tsize; j++)
			{
				int cost = Note.getInterval(h[i], t[j]);
				
				v1[j + 1] = Math.min(v1[j] + OMIT_COST, 
								Math.min(v0[j + 1] + OMIT_COST,
										 v0[j] + cost) );
			}

			// copy v1 (current row) to v0 (previous row) for next iteration
			for (int j = 0; j < v0.length; j++)
				v0[j] = v1[j];
		}
		System.out.println(v1[tsize]);
		double diff = (2.0 * v1[tsize] / (Note.getNote(5).semi * BirdComputer.TWEET_MAXLENGTH) ) - 1 ;
		System.out.printf("Difference: %f\n", diff);
		return diff;
	}
	
	public static double compareFAKE() {
		Random random = new Random();
		double x = 2 * (random.nextDouble()) - 1;
		return x;
	}
	
	/**
	 * TODO this man, this...
	 * @param tweet
	 */
	public void learn(Tweet othertweet){
		if (this.equals(othertweet))
			return;
		
		if (this.setNotes < this.size() ) {
			if (this.setNotes < othertweet.size()){
				this.remove(setNotes);
				this.add(setNotes, othertweet.get(setNotes) );
				setNotes++;
			} else
				return;
		}
		else
			return;
	}
	
	public Tweet(Tweet t1, Tweet t2){
		this();
		Iterator<Note> it_t1 = t1.iterator();
		Iterator<Note> it_t2 = t1.iterator();
		Random r = new Random();
		while (it_t1.hasNext() && it_t2.hasNext()){
			Note n1 = it_t1.next();
			Note n2 = it_t2.next();
			this.add( r.nextBoolean() ? n1 : n2);
		}
		
		while (it_t1.hasNext()){
			this.add(it_t1.next());
		}
		while (it_t2.hasNext()){
			this.add(it_t2.next());
		}
		
		this.setNotes = Math.min(t1.setNotes, t2.setNotes);
	}
	
	/**
	 * Testing for the tweet and tweet synth classes
	 * @param args
	 */
	public static void main(String[] args){
		BirdComputer testBird = new BirdComputer(5,0, new Object(), new Object() );
		testBird.tweetSynth.queueTweet(testBird.getTweet(), 0);
		
		try {
			Thread.sleep( (long) (testBird.getTweet().size() * Note.DURATION_SUM * 1000) );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
}