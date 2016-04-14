package com.tweeter.app;

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
	public TweetPlayer tweetPlyr;
	public TweetQueue tweetQueue;
	
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
	private ArrayList<BirdComputer> testBirds;
	// private BirdState birdState;
	
	public static final int ID = 2;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.width = 500;
		this.height = 500;
		timePassed = 0;
		testBirds = new ArrayList<BirdComputer>();
		// birdState = BirdState.NORMAL;
		font = new AngelCodeFont("fonts/demo2.fnt","fonts/demo2_00.tga");
		notesToAdd = new ArrayList<Character>();
		tweetTree = new TweetQueue(10);
		tweetPlyr = new TweetPlayer();
	}
	
	private void createMap(){
		
		this.mapSizeX = Integer.parseInt(NewGameSettingsState.sizeText);
		this.mapSizeY = Integer.parseInt(NewGameSettingsState.sizeText);
		this.npcBirdCount = Integer.parseInt(NewGameSettingsState.npcText);
		this.birdEnergyLimit = Integer.parseInt(NewGameSettingsState.birdEnergyText);
		map = new Map(mapSizeX,mapSizeY);
		
		
		for (int k = 0; k < mapSizeX; k++){
			for(int l = 0; l < mapSizeY; l++){
				Cell c = new Cell(k,l);
				System.out.println("created cell at "+k*(width/mapSizeX) + ", "+ l*(height/mapSizeY));
				map.addToGrid(c,k,l);
			}
		}
		
		this.userBird = new BirdPlayer(0,1,true,birdEnergyLimit);
		map.addBird(userBird);
		
		map.setNeighbors();
		
//		Bird b = new BirdComputer(4,4);
//		map.addBird(b);
//		Bird b2 = new BirdComputer(5,5);
//		map.addBird(b2);
		
		Random random = new Random();
		for (int i = 0; i<npcBirdCount; i++) {
			BirdComputer b = new BirdComputer(random.nextInt(mapSizeX), random.nextInt(mapSizeY));
			testBirds.add(b);
			map.addBird(b);
			tweetTree.addTweet(b.tweet, b.getPosX(), b.getPosY(), b);
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
					if (c.getBird().getBirdState() == BirdState.MATE) { graphics.setColor(Color.pink); }
					else if (c.getBird().getBirdState() == BirdState.ATTACK) { graphics.setColor(Color.red); }
					else if (c.getBird().getBirdState() == BirdState.TWEET) { graphics.setColor(Color.yellow); }
					else { graphics.setColor(Color.cyan); }
				} else {
					graphics.setColor(Color.white);
				}
				
				//TODO real number arithmetic is real
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
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
			
			// TODO find out how to resize window
			/*/  Make sure tiles fit in window
			//  window_width - (window_width mod size_of_board)
			//  
			newWidth = this.width % mapSizeX ;  
			newHeight = this.height % mapSizeY ;
			
			
			assert width==height : "Window Width does not match Height";*/
		outerloop:
		for (BirdComputer b : testBirds) {
			if (b.getBirdState() == BirdState.DEFAULT) {
				b.setMovingTowards(null);
				b.setStateTime(b.getStateTime()+delta);
				timePassed += delta;
								
				if (b.getStateTime() > 5000) {
					b.setStateTime(0);
					b.setBirdState(BirdState.LISTEN);
				} else if (timePassed > 1250){
					timePassed = 0;
					b.randomMove(map);
				}
			} //end of MOVING state 
			
			else if (b.getBirdState() == BirdState.LISTEN) {
				b.setStateTime(b.getStateTime()+delta);
				
				if (b.getStateTime() > 1250) {
					b.setStateTime(0);
					
					if (b.getMovingTowards() == null) {
						b.randomMove(map);
						TweetNode node = tweetTree.listen(b.getPosX(), b.getPosY());
						if (node!=null) {
							if (node.bird.getBirdState() == BirdState.LISTEN
									&& Tweet.compare(node.tweet, b.tweet) >= 0) {
								if (node.bird.movingTowards == null) {
									b.setMovingTowards(node.bird);
									node.bird.setMovingTowards(b);
								}
							}
						}
						else {
							b.randomMove(map);
						}
					}
					else {
						Bird target = b.getMovingTowards();
						if (!target.equals(b)) {
							double distance = Math.hypot(b.getPosX()-target.getPosX(), b.getPosY()-target.getPosY());
							if (distance <= Math.sqrt(2)) {
								int x, y;
								if (b.getPosX() - 1 > 0) x = b.getPosX() - 1; else x = 0;
								if (b.getPosY() - 1 > 0) y = b.getPosY() - 1; else y = 0;
								for (; x <= b.getPosX()+1; x++) {
									for (; y <= b.getPosY()+1; y++) {
										Cell c = map.getCellAt(x, y);
										if (!c.hasBird()) {
											BirdComputer spawn = new BirdComputer(x,y); 
											tweetTree.addTweet(spawn.tweet, spawn.getPosX(), spawn.getPosY(), spawn);
											spawn.setTweet(b.tweet);
											spawn.setBirdState(BirdState.DEFAULT);
											testBirds.add(spawn);
											map.addBird(spawn);
											target.setBirdState(BirdState.DEFAULT);
											b.setBirdState(BirdState.DEFAULT);
											break outerloop;
										}
									}
								} // end of nested for loop that spawns bird
							}
							else {
								b.moveTowards(map);
							}
						}
						else {
							b.randomMove(map);
							b.setBirdState(BirdState.DEFAULT);
						}
					}
				} // end of if (b.getStateTime() > 1250)
			} // end of NORMAL state
			
			else if (b.getBirdState() == BirdState.MATE) {
				//b.setMovingTowards(tweetTree.listen(b.getPosX(), b.getPosY()).bird);
				
				b.setMovingTowards(userBird);
				b.setStateTime(b.getStateTime()+delta);
				timePassed += delta;
								
				if (b.getStateTime() > 6000) {
					b.setStateTime(0);
					b.setBirdState(BirdState.LISTEN);
				} else if (timePassed > 750){
					timePassed = 0;
					b.moveTowards(map);
				}
			} // end of MATING state
			
			else if (b.getBirdState() == BirdState.TWEET) {
				tweetTree.addTweet(b.getTweet(), b.getPosX(), b.getPosY(), b);
				b.setBirdState(BirdState.LISTEN);
			} // end of TWEET state
			
		} // end of for each bird loop
		
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
				userBird.setTweet(new Tweet(cs));
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
		if(key == Input.KEY_X) {
			for (BirdComputer b : testBirds) { b.setBirdState(BirdState.MATE); }
			System.out.println("*****NEW***** Birds want to mate!");
		}
		if(key == Input.KEY_T) {
			userBird.setBirdState(BirdState.TWEET);
			tweetTree.addTweet(userBird.getTweet(), userBird.getPosX(), userBird.getPosY(), userBird);
			System.out.println("##### added to tweetQueue");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			userBird.setBirdState(BirdState.LISTEN);
		}
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
	

}
