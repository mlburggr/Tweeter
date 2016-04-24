package com.tweeter.app.birds;

import org.newdawn.slick.Color;

public enum BirdMood { 
	NEUTRAL(Color.cyan), MATE(Color.pink), ATTACK(Color.red), PLAYER(Color.gray);
	
	public final Color color;
	
	BirdMood(Color color){
		this.color = color;	
	}
}
