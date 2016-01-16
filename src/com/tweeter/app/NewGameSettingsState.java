package com.tweeter.app;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class NewGameSettingsState extends BasicGameState {
	
	public static final int ID = 1;
	
	private TextField sizeField;
	private AngelCodeFont font;
	private Image playButtonImage;
	private MouseOverArea playButtonArea;
	private StateBasedGame game;
	public static String sizeText;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
		font = new AngelCodeFont("fonts/demo2.fnt","fonts/demo2_00.tga");
		sizeField = new TextField(container,font,100,100,50,50);
		playButtonImage = new Image("res/start.jpg");
		playButtonArea = new MouseOverArea(container, playButtonImage, 200, 200);
		
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
		if(font == null){
			this.init(container,game);
			return;
		}
		sizeField.render(container, graphics);
		playButtonImage.draw(200,200);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount){
		super.mouseClicked(button, x, y, clickCount);
		this.sizeText = sizeField.getText();
		if(sizeText.equals("")){
			System.out.println("Enter a value!");
			return;
		}
		int sizeInt = Integer.parseInt(sizeText);
		if(sizeInt < 5){
			System.out.println("Must be larger than 4");
			return;
		}
		if (x >= playButtonArea.getX() && 
			x <= playButtonArea.getX() + playButtonArea.getWidth() && 
			y >= playButtonArea.getY() && 
			y <= playButtonArea.getY() + playButtonArea.getHeight()) {
				game.enterState(TweeterState.ID);
		}
		
	}


	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}
	

}
