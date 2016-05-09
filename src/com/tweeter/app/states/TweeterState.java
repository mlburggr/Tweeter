package com.tweeter.app.states;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.tweeter.app.birds.*;
import com.tweeter.app.globals.*;
import com.tweeter.app.sound.*;

@SuppressWarnings("static-access")
public class TweeterState extends BasicGameState {

	private int width;
	private int height;
	private AngelCodeFont font;
	private BirdPlayer userBird;
	public int mapSize = 1;
	private int points = 0;
	private int birdEnergyLimit = 100;
	private String noteToDraw = "";
	private ArrayList<Thread> threads;
	private int npcBirdCount = 0;
	
	//Added by Nick
	public static GlobalTweetPlayer tweetPlyr;
	public static TweetQueue tweetQueue;
	public static Random random = new Random(42);
	
	/**
	 * Game Modes
	 * ------
	 * These modes are used for processing input.
	 * They are defined by an integer. May change to
	 * a special type or enumeration later.
	 * 
	 * 1: Move Mode -- the player can move around and
	 * 					enter Composition Mode using tab
	 * 2: Composition Mode --  reads keys Q-R and A-F to correspond
	 * 					to different notes and add it to
	 * 					the player's tweet. When Tab is pressed again,
	 * 					tweet compositon mode is exited and the player
	 * 					bird will tweet the new tweet.
	 */
	private int gameMode = 1; 

	private int moveTimePassed;
	private int energyTimePassed;
	private Object moveLock;
	private Object energyLock;

	public static final int ID = 2;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.width = 500;
		this.height = 500;
		font = new AngelCodeFont("fonts/demo2.fnt","fonts/demo2_00.tga");

		this.mapSize = NewGameSettingsState.mapSize ;
		this.npcBirdCount = NewGameSettingsState.npcSize ;
		this.birdEnergyLimit = NewGameSettingsState.birdEnergy;
		
		Global.map = new Map(mapSize, mapSize, width, height);
		Global.tweetQueue = new TweetQueue(20) ;
		
		//Bird.ENERGY_RECOVERY = 100 / birdEnergyLimit;
		Bird.ENERGY_COST = Bird.ENERGY_RECOVERY / 4;
		
		// Initialize Player bird and add a synth for it to the tweet player
		this.userBird = new BirdPlayer(mapSize/2, mapSize/2);

		Random random = new Random();
		for (int i = 0; i<npcBirdCount; i++) {
			BirdComputer b = new BirdComputer(random.nextInt(mapSize), random.nextInt(mapSize), energyLock, moveLock); 
			threads.add( new Thread(b) ); }
		
		energyTimePassed = 0;
		moveTimePassed = 0;
		
		for (Thread thread : threads)
			thread.start();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {		
		// render birds
		for(int j = 0; j < mapSize; j++){
			for(int k = 0; k < mapSize; k++){
				Cell c = Global.map.getCellAt(j, k);
				if(c.hasBird())
					graphics.setColor( c.getBird().getMood().color );

				graphics.fillRect(j*(width/mapSize), k*(height/mapSize), (width/mapSize), (height/mapSize));
			}
		}


		graphics.setColor(new Color(56,56,56));
		for (int i = 0; i <= this.width; i += (height/mapSize)){
			graphics.drawLine(i, 0, i, this.height);
		}
		for (int i = 0; i <= this.height; i += (width/mapSize)){
			graphics.drawLine(0, i, this.width, i);
		}
		//TODO implement points system
		//font.drawString(350, 5, "Points:"+points, Color.black);
		graphics.setColor( Color.transparent.green );
		graphics.drawRect(350, 35, userBird.getEnergy(), 10);
		
		graphics.setColor(Color.transparent.red);
		graphics.drawRect(350, 50, userBird.getHealth(), 10);



		if(gameMode == 2) { //Composition Mode overlay
			graphics.setColor(Color.transparent.gray);
			graphics.fillRect(400, 450, 300, 150);
			if(noteToDraw != null){
				font.drawString(400, 450, noteToDraw);
			}
		}
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, 100, 50);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		moveTimePassed += delta;
		energyTimePassed += delta;
		if (energyTimePassed > Bird.ENERGY_COOLDOWN){
			energyLock.notifyAll();
			userBird.increaseEnergy();
			energyTimePassed = 0;	}
		
		else if (moveTimePassed > BirdComputer.MOVE_COOLDOWN){
			moveLock.notifyAll();
			energyTimePassed = 0;	}

		

	}

	public void keyReleased(int key, char c){
		System.out.println(key+" "+c);
		System.out.println("mode is "+gameMode);
		boolean exiting = false;
		if(gameMode == 2){
			switch(key){
			case Input.KEY_Q:
				//add note
				userBird.getTweet().add(Note.Q);
				noteToDraw += "Q";
				break;
			case Input.KEY_W:
				//add note
				userBird.getTweet().add(Note.W);
				noteToDraw += "W";
				break;
			case Input.KEY_E:
				//add note
				userBird.getTweet().add(Note.E);
				noteToDraw += "E";
				break;
			case Input.KEY_A:
				//add note
				userBird.getTweet().add(Note.A);
				noteToDraw += "A";
				break;
			case Input.KEY_S:
				//add note
				userBird.getTweet().add(Note.S);
				noteToDraw += "S";
				break;
			case Input.KEY_D:
				//add note
				userBird.getTweet().add(Note.D);
				noteToDraw += "D";
				break;
			case Input.KEY_TAB:
				//exit tab composition mode
				gameMode = 1;
				exiting = true;
				noteToDraw = "";
				userBird.tweet();
				break;
			}
			if (!exiting && userBird.getTweet().size() > Bird.TWEET_MAXLENGTH){
				gameMode = 1;
				exiting = true;
				noteToDraw = "";
				userBird.tweet();
			}

		}

		if(gameMode == 1 && !exiting){
			if(userBird.getEnergy() >= Bird.ENERGY_COST){
				switch(key){
				case Input.KEY_UP:
					Global.map.moveUp(userBird);
					System.out.println("up");
					break;
				case Input.KEY_DOWN:
					Global.map.moveDown(userBird);
					System.out.println("down");
					break;
				case Input.KEY_LEFT:
					Global.map.moveLeft(userBird);
					System.out.println("left");
					break;
				case Input.KEY_RIGHT:
					Global.map.moveRight(userBird);
					System.out.println("right");
					break;
				case Input.KEY_TAB:
					gameMode = 2;
				}

			}
			userBird.setEnergy(userBird.getEnergy() - Bird.ENERGY_COST);
		}

	}

	public int getCurrentMode(){
		return gameMode;
	}

	@Override
	public int getID() {
		return this.ID;
	}
}
