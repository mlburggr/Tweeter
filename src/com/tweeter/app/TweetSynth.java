package com.tweeter.app;


import java.util.Random;

import com.jsyn.data.Function;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.*;


public class TweetSynth extends Circuit{
	// Constants
	private static Random r = new Random(42);
	private static final int HARMONIC1 = 3;
	private static final int HARMONIC2 = 5;
	private static final int NUMUNITS = 3;

	//Holy shit I can't believe that works...
	private static Function harms[] = 
		{ null
		, new Function() { public double evaluate( double x ){
			return  HARMONIC1 * x;}}
		, new Function() { public double evaluate( double x ){
			return  HARMONIC2 * x;}}
		};
	
	// Ports
	public UnitOutputPort output;
	
	private PassThrough freqPassThru;

	private FunctionEvaluator[] harmUnits = new FunctionEvaluator[NUMUNITS];
	
	private UnitDivider dividerUnit;
	private Pan panUnit;

	// Oscillators
	private SineOscillator[] sineOscs = new SineOscillator[NUMUNITS];
	
	//Envelopes for playing
	private VariableRateMonoReader tweetFreqEnv;	
	private VariableRateMonoReader panEnv;
	private VariableRateMonoReader ampEnv;

	// Amplitude Envelope Players
	private VariableRateMonoReader[] ampEnvs = new VariableRateMonoReader[NUMUNITS];

	// Data for amp Envelopes	// First Envelope
	private  double[][] ampDats = {{ Note.DURATION_1/6, 1,
									 5 * Note.DURATION_1 / 6,  0.5,
									 Note.DURATION_2,  0.6,
									 Note.DURATION_3,  0},
								// Second Envelope
								   { Note.DURATION_1, 0,
								     Note.DURATION_2 + Note.DURATION_3,  1},
								// Third Envelope
								   { Note.DURATION_SUM - Note.DURATION_3,  0,
								     Note.DURATION_3, 1}};
	
	private SegmentedEnvelope [] ampEnvDats = new SegmentedEnvelope[NUMUNITS];	

	/**
	 * Create the circuit (starts from output to input basically)
	 */
	public TweetSynth() {
		super();
		
		
		
		// set up spatial panning envelope
		add( panUnit = new Pan() );
		add( panEnv = new VariableRateMonoReader() );
		panEnv.output.connect( panUnit.pan );

		// set up output leveling
		add( dividerUnit = new UnitDivider() );

		// Now set up flow of output 
		addPort( output = panUnit.output );
		add( ampEnv = new VariableRateMonoReader() );
		
		ampEnv.output.connect( dividerUnit.amplitude );
		
		dividerUnit.outputMono.connect( panUnit.input );

		
		// set up frequency envelope for (the general note)
		add( freqPassThru = new PassThrough() );		
		add( tweetFreqEnv = new VariableRateMonoReader() );
		tweetFreqEnv.output.connect( freqPassThru.input );

		//	Initialize the *real* unit generators
		for (int i = 0; i < NUMUNITS; i++) {
			add( sineOscs[i]  = new SineOscillator(440, 1));
			add( ampEnvs[i] = new VariableRateMonoReader() );

			//figure out where to queue data
			ampEnvDats[i] = new SegmentedEnvelope(ampDats[i]);
			ampEnvs[i].dataQueue.queueLoop(ampEnvDats[i]);
			ampEnvs[i].output.connect( sineOscs[i].amplitude );
			dividerUnit.addInput( sineOscs[i].output );
			
			if ( harms[i] != null ){
				add( harmUnits[i] = new FunctionEvaluator() );
				harmUnits[i].function.set( harms[i] );
				freqPassThru.output.connect( harmUnits[i].input );
				harmUnits[i].output.connect( sineOscs[i].frequency );
			} else
				freqPassThru.output.connect( sineOscs[i].frequency );}
	}
	
	/**
	 * change the frequency envelope of this synth
	 * 
	 * @param tweet
	 */
	public void queueTweet(Tweet tweet, double xposition, double bposition, double delay){
			Note [] tweetArr = tweet.toArray( new Note [0]);
			
			// set envelope data for pan and frequency
			double [] panDat = { 0, xposition } ; 
			double [] ampDat = { 0, bposition } ;
			
			double [] tweetFreqDat = new double[4 * (tweetArr.length+1) + 2]; 
			
			// Translate tweet
			tweetFreqDat[0] = delay *  7;
			tweetFreqDat[1] = 0;
			for (int i =2 ,j = 0; i < (tweetArr.length * 4) + 2; i += 4, j++){
				tweetFreqDat[i] = Note.DURATION_3 ;
				tweetFreqDat[i+1] = Note.BASE * (Math.pow(Note.SEMITONE, tweetArr[j].semi));
				tweetFreqDat[i+2] = Note.DURATION_SUM - Note.DURATION_3; 
				tweetFreqDat[i+3] = tweetFreqDat[i+1];
				System.out.printf("%fhz for %fsecs\n", tweetFreqDat[i+1], tweetFreqDat[i+2]);}
			// end it son
			tweetFreqDat[tweetFreqDat.length - 4] = 0;
			tweetFreqDat[tweetFreqDat.length - 3] = 0;
			tweetFreqDat[tweetFreqDat.length - 2] = Note.DURATION_SUM * tweetArr.length;
			tweetFreqDat[tweetFreqDat.length - 1] = 0;
			
			// queue tweet data in envelopes
			
			tweetFreqEnv.dataQueue.queue( new SegmentedEnvelope(tweetFreqDat) );
			panEnv.dataQueue.queue( new SegmentedEnvelope(panDat) );
			ampEnv.dataQueue.queue( new SegmentedEnvelope(ampDat) );

	}

}
