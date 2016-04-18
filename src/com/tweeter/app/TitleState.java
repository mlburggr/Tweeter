package com.tweeter.app;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.TrueTypeFont;


public class TitleState extends BasicGameState {
	public static final int ID = 0;
	private AngelCodeFont font;
	private StateBasedGame game;
	private Image startImage;
	private Image bgImage;
	private MouseOverArea startArea;
	

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
		font = new AngelCodeFont("fonts/demo2.fnt","fonts/demo2_00.tga");
		startImage = new Image("res/Play.png");
		bgImage = new Image("res/background.png");
		startArea = new MouseOverArea(container, startImage, 200, 200);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
		if(startImage == null || font == null){
			this.init(container,game);
			return;
		}
		graphics.drawImage(bgImage,0,0);
		graphics.setFont(font);
		font.drawString(200, 50, "Tweeter", Color.white);
		graphics.setColor(Color.white);
		startImage.draw(200,200);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int a) throws SlickException {
		
		
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount){
		super.mouseClicked(button, x, y, clickCount);
		
		if(x >= startArea.getX() && x <= startArea.getX() + startArea.getWidth() 
			&& 
			y >= startArea.getY() && y <= startArea.getY() + startArea.getHeight()){
			System.out.println("Button pressed!");
			game.enterState(NewGameSettingsState.ID);
		}
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
