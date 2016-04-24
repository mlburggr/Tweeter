package com.tweeter.app.sound;

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
	private UnitDividerStereo mixer;
	
	/**
	 * Starts up all the juicy synth infrastructure
	 */
	public GlobalTweetPlayer() {
		synth = JSyn.createSynthesizer();
		synth.start();
		synth.add( lineOut = new LineOut() );
		synth.add( mixer = new UnitDividerStereo() );
		
		mixer.outputMono.connect(0, lineOut.input, 0);
		mixer.outputStereo.connect(0, lineOut.input, 1);
		
		//iterate on bird list to add synths and envelopes to the hashes.
		lineOut.start();
	}

	/**
	 * Add a new bird's synth to the input pool
	 * 
	 * @param ts : bird's tweet synth
	 */
	public synchronized void add(TweetSynth ts){
		mixer.addInput( ts.output );
	}
	
	/**
	 * remove a dead bird's synth from the input pool
	 * 
	 * @param ts : bird's tweet synth
	 */
	public synchronized void remove(TweetSynth ts){
		mixer.removeInput( ts.output );
	}
}