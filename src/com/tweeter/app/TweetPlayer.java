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



/**
 * Remove this class, it is redundant
 * @author nick
 *
 */
public class TweetPlayer {

	
	
	
	private LineOut lineOut;
	private Synthesizer synth;
	private TweetSynth tweetSynth;
	private SegmentedEnvelope tweetFreqEnvDat;
	private VariableRateMonoReader tweetFreqEnv;
	
	private SegmentedEnvelope panEnvDat;
	private VariableRateMonoReader panEnv;

	
	/**
	 * Initialize tweet player, start main synthesizer, add line out.
	 */
	public TweetPlayer() {
		synth = JSyn.createSynthesizer();
		synth.stop();
		synth.start();
		synth.add( lineOut = new LineOut() );
	}
	
	// remove this
	public void playTweet(Tweet t, int x, int y){
		Note [] tweet = t.toArray( new Note [0]);
		synth.add( tweetSynth = new TweetSynth() );
		
		double [] tweetFreqDat = new double[4 * (tweet.length+1)]; 
		
		// Translate tweet
		for (int i =0 ,j = 0; i < tweet.length * 4; i += 4, j++){
			tweetFreqDat[i] = 0.0 ;
			tweetFreqDat[i+1] = Note.BASE * (Math.pow(Note.SEMITONE, tweet[j].semi));
			tweetFreqDat[i+2] = Note.DURATION; 
			tweetFreqDat[i+3] = tweetFreqDat[i+1];
			System.out.printf("%fhz for %fsecs\n", tweetFreqDat[i+1], tweetFreqDat[i+2]);}
		// end it son
		tweetFreqDat[tweetFreqDat.length - 4] = 0;
		tweetFreqDat[tweetFreqDat.length - 3] = 0;
		tweetFreqDat[tweetFreqDat.length - 2] = Note.DURATION * tweet.length;
		tweetFreqDat[tweetFreqDat.length - 1] = 0;
		
		synth.add( tweetFreqEnv = new VariableRateMonoReader() );
		tweetFreqEnv.output.connect(tweetSynth.frequency);
		
		//load tweet data to envelope
		tweetFreqEnvDat = new SegmentedEnvelope(tweetFreqDat);
		
		//Start playing tweet
		tweetFreqEnv.dataQueue.queueLoop(tweetFreqEnvDat, 0, tweetFreqEnvDat.getNumFrames());
		
		double [] panDat = { 0, x } ; 
		synth.add( panEnv = new VariableRateMonoReader() );
		panEnv.output.connect(tweetSynth.pan);
		panEnvDat = new SegmentedEnvelope(panDat);
		panEnv.dataQueue.queue(panEnvDat, 0, panEnvDat.getNumFrames());
		
		
		
		tweetSynth.output.connect(0, lineOut.input, 0 );
		tweetSynth.output.connect(1, lineOut.input, 1 );
		
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
		tweetSynth.output.connect(1, lineOut.input, 1);
		
		lineOut.start();
		
		System.out.println("Reached!");
	}
}
