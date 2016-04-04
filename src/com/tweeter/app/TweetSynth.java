package com.tweeter.app;

import com.jsyn.*;
import com.jsyn.data.Function;
import com.jsyn.data.SegmentedEnvelope;
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
		{ null
		, new Function() { public double evaluate( double x ){
			return  HARMONIC1 * x;}}
		, new Function() { public double evaluate( double x ){
			return  HARMONIC2 * x;}}
		};

	private static Function divider = 
			new Function() { 
		public double evaluate( double x ){
			return  x/NUMUNITS; 
		} 
	};


	// Ports
	public UnitInputPort frequency;
	public UnitInputPort amplitude;
	public UnitOutputPort output;
	private PassThrough freqPassThru;
	private PassThrough sigPassThru;

	private FunctionEvaluator[] harmUnits = new FunctionEvaluator[NUMUNITS];
	private FunctionEvaluator dividerUnit;

	// Oscillators
	private SineOscillator[] sineOscs = new SineOscillator[NUMUNITS];

	// Amplitude Envelope Players
	private VariableRateMonoReader[] ampEnvs = new VariableRateMonoReader[NUMUNITS];

	// Data for amp Envelopes				 // First Envelope
	private  double[][] ampDats = {{ 0.02, 1,
		0.2,  0.5,
		0.1,  0.6,
		0.1,  0},
		// Second Envelope
		{ 0.32, 1,
			0.1,  0},
			// Third Envelope
			{ 0.4,  1,
				0.02, 0}};
	private SegmentedEnvelope [] ampEnvDats = new SegmentedEnvelope[NUMUNITS];	


	public TweetSynth() {
		add( sineOscs[0] = new SineOscillator(110, 1));
		addPort( frequency = sineOscs[0].frequency );
		addPort( output = sineOscs[0].output );
		addPort( amplitude = sineOscs[0].amplitude );
		// starting from output to input

		// set up output leveling
		add( dividerUnit = new FunctionEvaluator() );
		dividerUnit.function.set(divider);
		addPort( amplitude = dividerUnit.amplitude );

		// Now set up flow of output 
		addPort( output = dividerUnit.output );
		add( sigPassThru = new PassThrough() );
		sigPassThru.output.connect(dividerUnit.input);

		// set up global frequency port for (the general note)
		add( freqPassThru = new PassThrough() );
		addPort( frequency = freqPassThru.input );



		//	Initialize the *real* unit generators
		for (int i = 0; i < NUMUNITS; i++) {
			add( sineOscs[i]  = new SineOscillator(440, 1));
			//ampEnvs[i] = new VariableRateMonoReader();

			//figure out where to queue data
			//ampEnvDats[i] = new SegmentedEnvelope(ampDats[i]);
			//ampEnvs[i].dataQueue.queue(ampEnvDats[i]);
			//ampEnvs[i].output.connect( sineOscs[i].amplitude );
			sineOscs[i].output.connect(sigPassThru.input);

			if (i > 0){
				add( harmUnits[i] = new FunctionEvaluator() );
				harmUnits[i].function.set( harms[i] );
				freqPassThru.output.connect( harmUnits[i].input );
				harmUnits[i].output.connect( sineOscs[i].frequency );
			} else
				freqPassThru.output.connect( sineOscs[0].frequency );}
	}
	TweetSynth(boolean simple){
	}

}

