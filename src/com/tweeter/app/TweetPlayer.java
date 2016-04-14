package com.tweeter.app;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.FilterStateVariable;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.WhiteNoise;
import com.tweeter.app.Note;
import com.tweeter.app.Tweet;
import com.tweeter.app.TweetSynth;

public class TweetPlayer {

	/**
	 * Frequency step of one semitone in hz. (2 ^ 1/12)
	 */
	public static final double SEMITONE = 1.059463094;	
	
	/**
	 * Base frequency (A3)
	 */
	public static final double BASE = 220.0;
	
	
	private LineOut lineOut;
	private Synthesizer synth;
	private TweetSynth tweetSynth;
	private SegmentedEnvelope tweetFreqEnvDat;
	private VariableRateMonoReader tweetFreqEnv;
	
	
	/**
	 * Initialize tweet player, start main synthesizer, add line out.
	 */
	public TweetPlayer() {
		synth = JSyn.createSynthesizer();
		synth.stop();
		synth.start();
		synth.add( lineOut = new LineOut() );
	}
	
	
	public void playTweet(Tweet t){
		Note [] tweet = t.toArray( new Note [0]);
		synth.add( tweetSynth = new TweetSynth() );
		
		double [] tweetFreqDat = new double[(4 * tweet.length) + 2]; 
		
		// Translate tweet
		for (int i =0 ,j = 0; i < tweet.length * 4; i += 4, j++){
			tweetFreqDat[i] = 0.0 ;
			tweetFreqDat[i+1] = BASE * (Math.pow(SEMITONE, tweet[j].semi));
			tweetFreqDat[i+2] = Note.DURATION; 
			tweetFreqDat[i+3] = tweetFreqDat[i+1];
			System.out.printf("%fhz for %fsecs\n", tweetFreqDat[i+1], tweetFreqDat[i+2]);}
		// end it son
		tweetFreqDat[tweetFreqDat.length - 2] = 0;
		tweetFreqDat[tweetFreqDat.length - 1] = 0;
		
		synth.add( tweetFreqEnv = new VariableRateMonoReader() );
		tweetFreqEnv.output.connect(tweetSynth.frequency);
		
		//load tweet data to envelope
		tweetFreqEnvDat = new SegmentedEnvelope(tweetFreqDat);
		
		//Start playing tweet
		tweetFreqEnv.dataQueue.queue(tweetFreqEnvDat, 0, tweetFreqEnvDat.getNumFrames());
		
		tweetSynth.output.connect(0, lineOut.input, 0);
		tweetSynth.output.connect(0, lineOut.input, 1);
		
		lineOut.start();
		
		System.out.println("Reached!");
	}
	public void playTweet(Tweet t, boolean b){
		Note [] tweet = t.toArray( new Note [0]);
		synth.add( tweetSynth = new TweetSynth(b) );
		
		/*
		double [] tweetFreqDat = new double[4 * tweet.length]; 
		
		// Translate tweet
		for (int i =0 ,j = 0; i < tweet.length * 4; i += 4, j++){
			tweetFreqDat[i] = BASE * (Math.pow(SEMITONE, tweet[j].semi));
			tweetFreqDat[i+1] = 0.0 ;
			tweetFreqDat[i+2] = tweetFreqDat[i];
			tweetFreqDat[i+3] = Note.DURATION; 
			System.out.printf("%fhz for %fsecs\n", tweetFreqDat[i], tweetFreqDat[i+3]);}
		
		
		synth.add( tweetFreqEnv = new VariableRateMonoReader() );
		tweetFreqEnv.output.connect(tweetSynth.frequency);
		
		//load tweet data to envelope
		tweetFreqEnvDat = new SegmentedEnvelope(tweetFreqDat);
		
		//Start playing tweet
		tweetFreqEnv.dataQueue.queue(tweetFreqEnvDat, 0, tweetFreqEnvDat.getNumFrames());
		*/
		VariableRateMonoReader ampEnv;
		SegmentedEnvelope ampEnvDat;
		
		double [] ampDat = {0.9, .2,
							1.0, 0.0};
		
		ampEnvDat = new SegmentedEnvelope(ampDat);
		
		synth.add( ampEnv = new VariableRateMonoReader() );
		ampEnv.output.connect( tweetSynth.amplitude );
		
		ampEnv.dataQueue.queue(ampEnvDat, 0, ampEnvDat.getNumFrames());
		
		tweetSynth.output.connect(0, lineOut.input, 0);
		tweetSynth.output.connect(0, lineOut.input, 1);
		
		lineOut.start();
		
		System.out.println("Reached!");
	}
}
