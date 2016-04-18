package com.tweeter.app;

import java.util.HashMap;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.VariableRateMonoReader;

public class GlobalTweetPlayer {
	
	private LineOut lineOut;
	private Synthesizer synth;
	private UnitDivider divider;
	
	private HashMap<Integer,TweetSynth> synths;
	private HashMap<Integer,VariableRateMonoReader> envelopes;
	
	public GlobalTweetPlayer( Bird [] birds) {
		synth = JSyn.createSynthesizer();
		synth.stop();
		synth.start();
		synth.add( lineOut = new LineOut() );
		synth.add( divider = new UnitDivider() );
		//iterate on bird list to add synths and envelopes to the hashes.
		
		synths = new HashMap<Integer,TweetSynth>();
		envelopes = new HashMap<Integer,VariableRateMonoReader>();
		
		for (Bird bird : birds){
			Integer id = bird.id;
			TweetSynth ts = new TweetSynth();
			VariableRateMonoReader env = new VariableRateMonoReader();
			synth.add(ts);
			synth.add(env);
			
			env.output.connect( ts.frequency );
			divider.addInput( ts.output );
			
			synths.put( id, ts);
			envelopes.put( id, env ); }
		
		divider.output.connect( lineOut.input );
	}

}
