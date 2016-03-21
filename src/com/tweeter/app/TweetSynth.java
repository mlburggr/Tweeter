package com.tweeter.app;

import com.jsyn.*;
import com.jsyn.data.Function;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.*;

public class TweetSynth extends Circuit{
	// Constants
	private static final int NUMUNITS = 3;
	private static final int HARMONIC1 = 3;
	private static final int HARMONIC2 = 5;
	 
	  	//Holy shit I can't believe that works...
	private static Function harms[] = 
		{ new Function() { public double evaluate( double x ){
	  								  return  HARMONIC1 * x;}}
		, new Function() { public double evaluate( double x ){
		  							  return  HARMONIC2 * x;}}
		};
	
	private static Function divider = 
			new Function() { public double evaluate( double x ){
							 return  x/NUMUNITS; } };

	
	// Ports
	public UnitInputPort frequency;
	public UnitOutputPort output;
	public PassThrough freqPassThru;
	public PassThrough sigPassThru;
	
	public FunctionEvaluator[] harmUnits;
	public FunctionEvaluator dividerUnit;
	
	// Oscillators
	private SineOscillator[] sineOscs;
	
	// Amplitude Envelope Players
	private VariableRateMonoReader[] ampEnvs;
	
	// Data for amp Envelopes				 // First Envelope
	private  double[][] ampEnvDats = {{ 0.02, 1,
										0.2,  0.5,
									    0.1,  0.6,
									    0.1,  0},
									         // Second Envelope
	                                  { 0.32, 1,
									    0.1,  0},
									         // Third Envelope
	                                  { 0.4,  1,
								        0.02, 0}};
		
	
	
	public TweetSynth() {
		super();
		
		
		freqPassThru = new PassThrough();
		sigPassThru =new PassThrough();
		addPort( frequency = freqPassThru.input );
		addPort( output = dividerUnit.output );
		
		//	Init Circuit
		for (int i = 0; i < NUMUNITS; i++) {
			add( sineOscs[i]  = new SineOscillator() );
			ampEnvs[i] = new VariableRateMonoReader();
			
			//figure out where to queue data
			//ampEnvs[i].dataQueue.
			ampEnvs[i].output.connect( sineOscs[i].amplitude );
			sineOscs[i].output.connect(dividerUnit.input);
			
			if (i > 0){
				add( harmUnits[i] = new FunctionEvaluator() );
				harmUnits[i].function.set( harms[i] );
				freqPassThru.output.connect( harmUnits[i].input );
				harmUnits[i].output.connect( sineOscs[i].frequency );

			} else
				freqPassThru.output.connect( sineOscs[0].frequency );

		}
		
	}


}
