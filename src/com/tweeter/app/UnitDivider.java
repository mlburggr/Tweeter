package com.tweeter.app;

import com.jsyn.data.Function;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.FunctionEvaluator;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.UnitGenerator;

public class UnitDivider extends Circuit {
	private int units = 1;
	
	private PassThrough sigPassThru;
	private FunctionEvaluator dividerGen;
	public UnitInputPort input;
	public UnitInputPort amplitude;
	public UnitOutputPort output;
	private Function divider;
	
	public UnitDivider(){
		units = 0;

		divider = null;

		add( dividerGen = new FunctionEvaluator() );
		add( sigPassThru = new PassThrough() );
				
		addPort( input = sigPassThru.input );
		addPort( amplitude = dividerGen.amplitude );
		
		sigPassThru.output.connect( dividerGen.input );
		
		addPort( output = dividerGen.output );
	}
	
	public void addInput(UnitOutputPort newUnit){
		newUnit.connect( input );
		units++;
		
		divider = new Function() { 
			public double evaluate( double x ){
		return  (x/units) - .1; }};
		
		dividerGen.function.set(divider);
	}
	
}
