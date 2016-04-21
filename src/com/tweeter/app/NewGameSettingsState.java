package com.tweeter.app;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
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
	private TextField npcField;
	private TextField birdEnergyField;
	private AngelCodeFont font;
	private Image playButtonImage;
	private MouseOverArea playButtonArea;
	private StateBasedGame game;
	public static String sizeText;
	public static String npcText;
	public static String birdEnergyText;
	private Image bgImage;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
		font = new AngelCodeFont("fonts/demo2.fnt","fonts/demo2_00.tga");
		sizeField = new TextField(container,font,100,100,50,40);
		npcField = new TextField(container,font,100,200,50,40);
		birdEnergyField = new TextField(container,font,100,300,50,40);
		playButtonImage = new Image("res/Play.png");
		playButtonArea = new MouseOverArea(container, playButtonImage, 200, 200);
		bgImage = new Image("res/background.png");
		
		
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
		if(font == null){
			this.init(container,game);
			return;
		}
		graphics.drawImage(bgImage, 0, 0);
		font.drawString(50, 50, "Size of Field (n x n):", Color.white);
		font.drawString(50, 150, "Inital NPCs:", Color.white);
		font.drawString(50, 250, "Max Energy:", Color.white);
		sizeField.render(container, graphics);
		npcField.render(container, graphics);
		birdEnergyField.render(container, graphics);
		playButtonImage.draw(200,200);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount){
		super.mouseClicked(button, x, y, clickCount);
		this.npcText = npcField.getText();
		this.sizeText = sizeField.getText();
		this.birdEnergyText = birdEnergyField.getText();
		
		if(sizeText.equals("") || npcText.equals("")
				|| birdEnergyText.equals("")){
			System.out.println("Enter a value!");
			return;
		}
		int sizeInt = Integer.parseInt(sizeText);
		if(sizeInt < 5){
			System.out.println("Board size must be larger than 4");
			return;
		}
		
		//This is really fucking lame, There's no abstraction for this?
		//no dude, slick2d is hardcore
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
