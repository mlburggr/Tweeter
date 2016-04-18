package com.tweeter.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class TweeterState extends BasicGameState {
	
	private int width;
	private int height;
	private Map map;
	private AngelCodeFont font;
	private BirdPlayer userBird;
	public int mapSizeX = 1;
	public int mapSizeY = 1;
	private int points = 0;
	private int birdEnergyLimit = 0;
	private String noteToDraw = "";
	private ArrayList<Character> notesToAdd;
	private int npcBirdCount = 0;
	
	//Added by Nick
	public static GlobalTweetPlayer tweetPlyr;
	public static TweetQueue tweetQueue;
		/*
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
	
	private int timePassed;
	public static ArrayList<BirdComputer> testBirds;
	public static Iterator<BirdComputer> iter;
	public static ArrayList<BirdComputer> birdsToAdd;
	public static ArrayList<BirdComputer> birdsToRemove;
	
	private final double NEUTRAL_MIN = -0.25;
	private final double NEUTRAL_MAX = 0.25;
	
	public static final int ID = 2;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.width = 500;
		this.height = 500;
		font = new AngelCodeFont("fonts/demo2.fnt","fonts/demo2_00.tga");
		notesToAdd = new ArrayList<Character>();
		
		tweetQueue = new TweetQueue(20);
		
		timePassed = 0;
		testBirds = new ArrayList<BirdComputer>();
		birdsToAdd = new ArrayList<BirdComputer>();
		birdsToRemove = new ArrayList<BirdComputer>();
		
	}
	
	private void createMap(){
		
		this.mapSizeX = Integer.parseInt(NewGameSettingsState.sizeText);
		this.mapSizeY = Integer.parseInt(NewGameSettingsState.sizeText);
		this.npcBirdCount = Integer.parseInt(NewGameSettingsState.npcText);
		this.birdEnergyLimit = Integer.parseInt(NewGameSettingsState.birdEnergyText);
		map = new Map(mapSizeX,mapSizeY);
		tweetPlyr = new GlobalTweetPlayer();
		
		
		for (int k = 0; k < mapSizeX; k++){
			for(int l = 0; l < mapSizeY; l++){
				Cell c = new Cell(k,l);
				System.out.println("created cell at "+k*(width/mapSizeX) + ", "+ l*(height/mapSizeY));
				map.addToGrid(c,k,l);
			}
		}
		// Initialize Player bird and add a synth for it to the tweet player
		this.userBird = new BirdPlayer(0, 1, true, birdEnergyLimit);
		map.addBird(userBird);
		tweetPlyr.add( userBird.getId() );
		
		map.setNeighbors();
		Random random = new Random();
		for (int i = 0; i<npcBirdCount; i++) {
			BirdComputer b = new BirdComputer(random.nextInt(mapSizeX), random.nextInt(mapSizeY));
			testBirds.add(b);
			map.addBird(b);
			tweetPlyr.add( b.id );

			//tweetQueue.addTweet(b.tweet, b.getPosX(), b.getPosY(), b);
		}
		
	
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
		//graphics.setBackground(new Color(78,78,78));
		if(map == null){
			createMap();
		}
		
		
		
		for(int j = 0; j < mapSizeX; j++){
			for(int k = 0; k < mapSizeY; k++){
				Cell c = map.getCellAt(j, k);
				if(c.hasBird() && c.getBird().isUserBird()){
					graphics.setColor(Color.gray);
//					if (c.getBird().getBirdState() == BirdState.NORMAL) { graphics.setColor(Color.gray); }
//					else if (c.getBird().getBirdState() == BirdState.TWEET) { graphics.setColor(Color.yellow); }
				} else if(c.hasBird()){
					if (c.getBird().getBirdState() == BirdState.TWEET) { graphics.setColor(Color.yellow); }
					else if (c.getBird().getBirdState() == BirdState.LISTEN) { 
						if (c.getBird().mood == BirdMood.MATE) { graphics.setColor(Color.pink); }
						else if (c.getBird().mood == BirdMood.ATTACK) { graphics.setColor(Color.red); }
						else if (c.getBird().mood == BirdMood.NEUTRAL) { graphics.setColor(Color.green); }
					}
					else if (c.getBird().getBirdState() == BirdState.LISTEN) { graphics.setColor(Color.blue); }
					else { graphics.setColor(Color.cyan); }
				} else {
					graphics.setColor(Color.white);
				}
				
				graphics.fillRect(j*(width/mapSizeX), k*(height/mapSizeY), (width/mapSizeX), (height/mapSizeY));
				
			}
		}
		
		
		graphics.setColor(new Color(56,56,56));
		for (int i = 0; i <= this.width; i += (height/mapSizeY)){
			graphics.drawLine(i, 0, i, this.height);
		}
		for (int i = 0; i <= this.height; i += (width/mapSizeX)){
			graphics.drawLine(0, i, this.width, i);
		}
		
		font.drawString(350, 5, "Points:"+points, Color.black);
		font.drawString(350, 35, "Energy:"+userBird.getEnergy(), Color.black);
		font.drawString(350, 65, "Health:"+userBird.getHealth(), Color.black);
		
		
		
		if(gameMode == 2) { //Composition Mode overlay
			graphics.setColor(Color.gray);
			graphics.fillRect(100, 100, 300, 150);
			if(noteToDraw != null){
				font.drawString(150, 150, noteToDraw);
			}
		}
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, 100, 50);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
			
		for (iter = testBirds.iterator(); iter.hasNext(); ) {
			BirdComputer b = iter.next();
			
			if (b.getBirdState() == BirdState.DEFAULT) {
				b.setMovingTowards(null);
				b.setStateTime(b.getStateTime()+delta);
				timePassed += delta;
								
				if (b.getStateTime() > 5000) {
					b.setStateTime(0);
					b.setBirdState(BirdState.TWEET);
				} else if (timePassed > 1250){
					timePassed = 0;
					
					b.moveRandom(map);
					
//					Bird partner = b.moveRandom(map);
//					if(partner != null) {
//						TweeterState.mate(b, partner, map);
//					}
					
				}
			} //end of DEFAULT state 
			
			if (b.getBirdState() == BirdState.NEWBORN) {
				b.setMovingTowards(null);
				b.setStateTime(b.getStateTime()+delta);
				timePassed += delta;
								
				if (b.getStateTime() > 3000) {
					b.setStateTime(0);
					b.setBirdState(BirdState.TWEET);
				} else if (timePassed > 1250){
					timePassed = 0;
					b.moveRandom(map);
//					Bird partner = b.moveRandom(map);
//					if(partner != null) {
//						TweeterState.mate(b, partner, map);
//					}
				}
			} //end of NEWBORN state 
			
			else if (b.getBirdState() == BirdState.TWEET) {
				b.setStateTime(b.getStateTime()+delta);
				
				if (b.getStateTime() > 2000) {
					b.setStateTime(0);
					
					b.tweet(tweetPlyr, tweetQueue, map.sizeX, userBird.posX);
					
					//b.moveRandom(map);
					
					b.setBirdState(BirdState.LISTEN);
				}
				
			} //end of TWEET state
			
			else if (b.getBirdState() == BirdState.LISTEN) {
				b.setStateTime(b.getStateTime()+delta);
				
				if (b.getStateTime() > 1250) {
					b.setStateTime(0);
				
					TweetNode tweetnode = tweetQueue.listen(b.getPosX(), b.getPosY());
				
					if (tweetnode != null) {
				
						// TODO Nick, do the learning process here!
				
						// TODO Nick, fix the compare method to return both (+) and (-) numbers
						
						//double compare = Tweet.compare(tweetnode.tweet, b.tweet);
						double compare = Tweet.compareFAKE();
						
						if (compare > NEUTRAL_MAX) {
							b.mood = BirdMood.MATE;
							b.moveToCoord(map, tweetnode.x0, tweetnode.y0);
							b.setBirdState(BirdState.TWEET);
						}
						else if (compare >= NEUTRAL_MIN) {
							b.mood = BirdMood.NEUTRAL;
							b.setBirdState(BirdState.TWEET);
						}
						else {
							b.mood = BirdMood.ATTACK;
							b.moveAwayCoord(map, tweetnode.x0, tweetnode.y0);
							b.setBirdState(BirdState.TWEET);
						}
				
					}
				}
				
			} // end of LISTEN state
			
		} // end of for each bird loop
		
		// This adds the new child birds to the array list after the for loop
		// is executed to prevent concurrent modification exception.
		testBirds.addAll(birdsToAdd);
		testBirds.removeAll(birdsToRemove);
		
	}

	public void keyReleased(int key, char c){
		System.out.println(key+" "+c);
		
		int currentPosX = userBird.getPosX();
		int currentPosY = userBird.getPosY();
		
		System.out.println("mode is "+gameMode);
		boolean exiting = false;
		if(gameMode == 2){
			switch(key){
			case Input.KEY_Q:
				//add note
				notesToAdd.add('q');
				noteToDraw += "Q";
				break;
			case Input.KEY_W:
				//add note
				notesToAdd.add('w');
				noteToDraw += "W";
				break;
			case Input.KEY_E:
				//add note
				notesToAdd.add('e');
				noteToDraw += "E";
				break;
			case Input.KEY_A:
				//add note
				notesToAdd.add('a');
				noteToDraw += "A";
				break;
			case Input.KEY_S:
				//add note
				notesToAdd.add('s');
				noteToDraw += "S";
				break;
			case Input.KEY_D:
				//add note
				notesToAdd.add('d');
				noteToDraw += "D";
				break;
			case Input.KEY_TAB:
				//exit tab composition mode
				gameMode = 1;
				exiting = true;
				noteToDraw = "";
				char[] cs = new char[notesToAdd.size()];
				for(int i=0; i<cs.length; i++){
					cs[i] = notesToAdd.get(i);
				}
				notesToAdd.clear();
				userBird.setTweet(new Tweet(cs));
				userBird.tweet(tweetPlyr,tweetQueue, map.sizeX, userBird.posX);
				break;
				
			}
		}
		
		if(gameMode == 1 && !exiting){
		if(userBird.getEnergy() >= 5){
		if(key == Input.KEY_UP){
				map.moveUp(userBird, currentPosX, currentPosY);
				System.out.println("up");
		}
		if(key == Input.KEY_DOWN){
			map.moveDown(userBird, currentPosX, currentPosY);
			System.out.println("down");
		}
		if(key == Input.KEY_LEFT){
			map.moveLeft(userBird, currentPosX, currentPosY);
			System.out.println("left");
		}
		if(key == Input.KEY_RIGHT){
			map.moveRight(userBird, currentPosX, currentPosY);
			System.out.println("right");
		}
		if(key == Input.KEY_TAB){
				gameMode = 2;
		}
//		if(key == Input.KEY_T) {
//			userBird.setBirdState(BirdState.TWEET);
//			tweetQueue.addTweet(userBird.getTweet(), userBird.getPosX(), userBird.getPosY(), userBird);
//			System.out.println("##### added to tweetQueue");
//			
//			userBird.setBirdState(BirdState.LISTEN);
//		}
		// userBird.setEnergy(userBird.getEnergy() - 5); TODO uncomment later
		}
		
		}
		

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}
	
	public int getCurrentMode(){
		return gameMode;
	}
	

//	private static void mate(BirdComputer dad, Bird mom, Map map) {
//		int xMin, yMin, xMax, yMax;
//		if (dad.getPosX() - 1 > 0) xMin = dad.getPosX() - 1; else xMin = 0;
//		if (dad.getPosY() - 1 > 0) yMin = dad.getPosY() - 1; else yMin = 0;
//		if (dad.getPosX() + 1 >= map.sizeX) xMax = dad.getPosX() - 1; else xMax = map.sizeX-1;
//		if (dad.getPosY() + 1 >= map.sizeY) yMax = dad.getPosY() - 1; else yMax = map.sizeY-1;
//		
//		for (int x = xMin; x <= xMax; x++) {
//			for (int y = yMin; y <= yMax; y++) {
//				Cell c = map.getCellAt(x, y);
//				if (!c.hasBird()) {
//						BirdComputer child = new BirdComputer(x,y, dad, mom); 
//						map.addBird(child);
//						break;
//				}
//			}
//		} // end of nested for loop that spawns bird	
//	}
	
}
