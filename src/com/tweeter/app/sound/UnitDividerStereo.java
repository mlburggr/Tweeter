package com.tweeter.app.sound;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.FunctionEvaluator;
import com.jsyn.unitgen.PassThrough;

public class UnitDividerStereo extends UnitDivider {
	
	private FunctionEvaluator dividerGenStereo;
	public UnitInputPort inputStereo;
	private PassThrough sigPassThruStereo;
	public UnitOutputPort outputStereo;
	
	/**
	 * construct new Mixer to take arbitrarily many units
	 * (but initially has none)
	 */
	public UnitDividerStereo(){
		super();

		add( dividerGenStereo = new FunctionEvaluator() );
		add( sigPassThruStereo = new PassThrough() );
		
		addPort( inputStereo = sigPassThruStereo.input );
		
		sigPassThruStereo.output.connect(0, dividerGenStereo.input, 0 );		
		addPort( outputStereo = dividerGenStereo.output );
		
	}
	
	/**
	 * Add a new input
	 *
	 * @param newUnit : unit to be added
	 */
	public void addInput(UnitOutputPort newUnit){
		super.addInput(newUnit);
		
		newUnit.connect(1, inputStereo, 0 );
		
		dividerGenStereo.function.set(divider);

	}
	
	/**
	 * Remove an input
	 *
	 * @param oldUnit : unit to be removed
	 */
	public void removeInput(UnitOutputPort oldUnit){
		super.removeInput(oldUnit);
		
		oldUnit.disconnect(1, inputStereo, 0 );
		
		dividerGenStereo.function.set(divider);
	}
	
}
