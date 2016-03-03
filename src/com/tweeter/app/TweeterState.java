package com.tweeter.app;

import java.util.ArrayList;
import java.util.Random;

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
	private BirdPlayer userBird;
	public int mapSizeX = 1;
	public int mapSizeY = 1;
	
	private int timePassed;
	private ArrayList<BirdComputer> testBirds;
	
	public static final int ID = 2;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.width = 500;
		this.height = 500;
		timePassed = 0;
		testBirds = new ArrayList<BirdComputer>();
	}
	
	private void createMap(){
		
		this.mapSizeX = Integer.parseInt(NewGameSettingsState.sizeText);
		this.mapSizeY = Integer.parseInt(NewGameSettingsState.sizeText);
		map = new Map(mapSizeX,mapSizeY);
		
		
		for (int k = 0; k < mapSizeX; k++){
			for(int l = 0; l < mapSizeY; l++){
				Cell c = new Cell(k,l);
				System.out.println("created cell at "+k*(width/mapSizeX) + ", "+ l*(height/mapSizeY));
				map.addToGrid(c,k,l);
			}
		}
		
		this.userBird = new BirdPlayer(0,1,true);
		map.addBird(userBird);
		
		map.setNeighbors();
		
//		Bird b = new BirdComputer(4,4);
//		map.addBird(b);
//		Bird b2 = new BirdComputer(5,5);
//		map.addBird(b2);
		
		Random random = new Random();
		for (int i = 0; i<3; i++) {
			BirdComputer b = new BirdComputer(random.nextInt(mapSizeX), random.nextInt(mapSizeY));
			testBirds.add(b);
			map.addBird(b);
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
				} else if(c.hasBird()){
					graphics.setColor(Color.black);
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
		
		timePassed += delta;
		if (timePassed > 1250) {
			timePassed = 0;
			for (BirdComputer b : testBirds) {
				b.randomMove(map);
			}
		}
		
	}
	
	public void keyReleased(int key, char c){
		System.out.println(key+" "+c);
		
		int currentPosX = userBird.getPosX();
		int currentPosY = userBird.getPosY();
		
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
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}
	

}
