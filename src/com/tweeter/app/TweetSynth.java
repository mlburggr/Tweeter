package com.tweeter.app;

import com.softsynth.jsyn.*;

public class TweetSynth extends SynthCircuit{
	// Line
	private LineOut line;
	
	// Oscillators
	private SineOscillator s1_Osc;
	private SineOscillator s2_Osc;
	private SineOscillator s3_Osc;
	
	// Envelopes
	private SynthEnvelope e1_Env;
	private SynthEnvelope e2_Env;
	private SynthEnvelope e3_Env;
	
	// Envelope player
	private EnvelopePlayer e1_Ply;
	private EnvelopePlayer e2_Ply;
	private EnvelopePlayer e3_Ply;
	
	// Data for Envelopes
	private static double[] e1_Dat = { 0.02, 1,
									   0.2,  0.5,
									   0.1,  0.6,
									   0.1,  0};
	
	private static double[] e2_Dat = { 0.32, 1,
									   0.1,  0};
	
	private static double[] e3_Dat = { 0.4,  1,
								       0.02, 0};
	/**
	 * Frequency step of one semitone in hz. (2 ^ 1/12)
	 */
	public static final double SEMITONE = 1.059463094;	
	
	/**
	 * Base frequency (A2)
	 */
	public static final double BASE = 110.0;		
	
	
	public TweetSynth() {
		super();
		try{
			Synth.startEngine(0);
			s1_Osc  = new SineOscillator();
		} catch( SynthException e )
		{
             System.out.println( "Caught " + e );
             e.printStackTrace();
		}
	}

}
