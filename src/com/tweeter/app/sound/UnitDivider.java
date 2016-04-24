package com.tweeter.app.sound;

import com.jsyn.data.Function;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.FunctionEvaluator;
import com.jsyn.unitgen.PassThrough;

public class UnitDivider extends Circuit {
	protected int units;
	
	protected PassThrough sigPassThru;
	protected FunctionEvaluator dividerGenMono;
	public UnitInputPort inputMono;
	public UnitInputPort amplitude;
	public UnitOutputPort outputMono;
	protected Function divider;
	
	/**
	 * construct new Mixer to take arbitrarily many units
	 * (but initially has none)
	 */
	public UnitDivider(){
		units = 0;

		divider = null;

		add( dividerGenMono = new FunctionEvaluator() );
		add( sigPassThru = new PassThrough() );
				
		addPort( inputMono = sigPassThru.input );
		addPort( amplitude = dividerGenMono.amplitude);
		
		sigPassThru.output.connect(0, dividerGenMono.input, 0 );		
		addPort( outputMono = dividerGenMono.output );
		
	}
	
	/**
	 * Add a new input
	 *
	 * @param newUnit : unit to be added
	 */
	public void addInput(UnitOutputPort newUnit){
		newUnit.connect(0, inputMono, 0 );
		
		units++;
		
		divider = new Function() { 
			public double evaluate( double x ){
		return  (x/units) - .1; }};
		
		dividerGenMono.function.set(divider);
	}
	
	/**
	 * remove an input
	 *
	 * @param oldUnit : unit to be added
	 */
	public void removeInput(UnitOutputPort oldUnit){
		oldUnit.disconnect(0, inputMono, 0 );
		
		units--;
		
		divider = new Function() { 
			public double evaluate( double x ){
		return  (x/units) - .1; }};
		
		dividerGenMono.function.set(divider);
	}
}
