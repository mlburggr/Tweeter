package com.tweeter.app;

import java.util.ArrayList;
import java.util.HashMap;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;


/**
 * This class holds all the individual tweet synths for 
 * each bird and connects them to the final output.
 * 
 * The synths are stored using a unique bird id and a hash map.
 * 
 * @author nick
 *
 */
public class GlobalTweetPlayer {
	
	private LineOut lineOut;
	private Synthesizer synth;
	private UnitDividerStereo divider;
	
	private HashMap<Integer,TweetSynth> synths;
	
	/**
	 * build the initial map of tweet synths from the 
	 * initial birds on the board
	 * 
	 * @param birds : the initial list of birds on the board
	 */
	public GlobalTweetPlayer( ArrayList<BirdComputer> birds) {
		this();
		
		for (Bird bird : birds){
			Integer id = bird.id;
			TweetSynth ts = new TweetSynth();
			synth.add(ts);
			divider.addInput( ts.output );
			
			synths.put( id, ts); }
	}
	
	/**
	 * Debugging constructor of gtp
	 */
	public GlobalTweetPlayer() {
		synth = JSyn.createSynthesizer();
		synth.start();
		synth.add( lineOut = new LineOut() );
		synth.add( divider = new UnitDividerStereo() );
		
		divider.outputMono.connect(0, lineOut.input, 0);
		divider.outputStereo.connect(0, lineOut.input, 1);
		
		//iterate on bird list to add synths and envelopes to the hashes.
		synths = new HashMap<Integer,TweetSynth>();
		lineOut.start();
	}

	/**
	 * Add a new bird's synth to the collection of synths
	 * 
	 * @param b_id new bird's unique id
	 */
	public void add(int b_id){
		Integer id = b_id;
		TweetSynth ts = new TweetSynth();
		synth.add(ts);
		divider.addInput( ts.output );
		synths.put( id, ts); }
	
	
	/**
	 * Gets the tweet synth associated with the bird's unique bird id
	 * 
	 * @param id : bird's unique id
	 * @return the synthesizer associated with the bird
	 */
	public TweetSynth getTweetSynth(int id){
		return synths.get( (Integer) id );}
}
