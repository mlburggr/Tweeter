package com.tweeter.app;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame{
	private Map map;
	private int timer;
	private int turn;
	public int width;
	private int height;
	private String fontPath;
	private UnicodeFont uFont;

	public Game(String title) {
		super(title);
		this.map = new Map(5,5);
	}


	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new TitleState());
		addState(new TweeterState());
		addState(new NewGameSettingsState());
		
	}
	
	public void setHeight(int h){
		this.height = h;
	}
	
	public void setWidth(int w){
		this.width = w;
	}

}
