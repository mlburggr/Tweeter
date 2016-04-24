package com.tweeter.app.globals;

import java.util.ArrayList;
import java.util.List;

import com.tweeter.app.birds.Bird;
import com.tweeter.app.birds.BirdMood;

public class Map {
	private final int sizeX;
	public int getSizeX() {
		return sizeX;
	}

	private final int sizeY;
	public int getSizeY(){
		return sizeY;
	}
	private Cell[][] grid;

	public Map(int sX, int sY, int width, int height){
		this.sizeX = sX;
		this.sizeY = sY;
		grid = new Cell[sX][sY];
		
		// Initialize the grid cells
		for (int k = 0; k < sizeX; k++){
			for(int l = 0; l < sizeY; l++){
				Cell c = new Cell(k,l);
				System.out.println("created cell at "+k*(width/sizeX) + ", "+ l*(height/sizeY));
				addToGrid(c,k,l); }}
		
		// Initialize the paths for the grid
		for(int j = 0; j < sizeX-1; j++){
			for(int k = 0; k < sizeY-1; k++){
				// get the list of neighbors
				List<Cell> neighbors = new ArrayList<Cell>();
				for (int dx = (j > 0 ? -1 : 0); dx <= (j < sizeX-1 ? 1 : 0); dx++){
					for (int dy = (k > 0 ? -1 : 0); dy <= (k < sizeY-1 ? 1 : 0); dy++){
						if (dx != 0 || dy != 0){
							neighbors.add(new Cell(j + dx, k + dy)); } } }
				
				Cell[] n = neighbors.toArray(new Cell[neighbors.size()]);
				grid[j][k].setNeighbors(n); }}
	}
	
	private void addToGrid(Cell c, int locX, int locY){
		grid[locX][locY] = c;
	}

	
	public synchronized Cell[][] getGrid(){
		return grid;
	}
	
	/**
	 * Move bird one space up
	 * checks to see if bird is on the right edge or 
	 * there is a bird above it
	 * 
	 * @param b : moving bird
	 * @return true if the bird moves, false if not
	 */
	public synchronized boolean moveUp(Bird b){
		int nextX = b.getPosX();
		int nextY = b.getPosY() - 1;
		
		if (nextX > sizeX - 1)
			return false;
		if (grid[nextX][nextY].hasBird())
			return false;
		
		grid[b.getPosX()][b.getPosY()].setBird(null, false);
		grid[nextX][nextY].setBird(b, true);
		b.setPosY( nextY );
		
		return true;
	}
	
	/**
	 * Move bird one space to the left
	 * checks to see if bird is on the right edge or
	 * there is a bird next to it
	 * 
	 * @param b : moving bird
	 * @return true if the bird moves, false if not
	 */
	public synchronized boolean moveLeft(Bird b){
		int nextX = b.getPosX() - 1;
		int nextY = b.getPosY();
		
		if (nextX < 0 )
			return false;
		if (grid[nextX][nextY].hasBird())
			return false;
		
		grid[b.getPosX()][b.getPosY()].setBird(null, false);
		grid[nextX][nextY].setBird(b, true);
		b.setPosX( nextX );
		
		return true;
	}
	
	/**
	 * Move bird one space to the right
	 * checks to see if bird is on the right edge or
	 * there is a bird next to it
	 * 
	 * @param b : moving bird
	 * @return true if the bird moves, false if not
	 */
	public synchronized boolean moveRight(Bird b){
		int nextX = b.getPosX() + 1;
		int nextY = b.getPosY();
		
		if (nextX == sizeX)
			return false;
		if (grid[nextX][nextY].hasBird())
			return false;
		
		grid[b.getPosX()][b.getPosY()].setBird(null, false);
		grid[nextX][nextY].setBird(b, true);
		b.setPosX( nextX );
		
		return true;
	}
	
	/**
	 * Move bird one space down
	 * checks to see if bird is on the bottom edge or there 
	 * is a bird below it
	 * 
	 * @param b : moving bird
	 * @return true if the bird moves, false if not
	 */
	public synchronized boolean moveDown(Bird b){
		int nextX = b.getPosX();
		int nextY = b.getPosY() + 1;
		
		if (nextY == sizeY)
			return false;
		if (grid[nextX][nextY].hasBird())
			return false;
		
		grid[b.getPosX()][b.getPosY()].setBird(null, false);
		grid[nextX][nextY].setBird(b, true);
		b.setPosY( nextY );
		
		return true;
	}

	/**
	 * Add bird to the grid
	 *  
	 * @param b : bird to add
	 */
	public void addBird(Bird b){
		grid[b.getPosX()][b.getPosY()].setBird(b,true);
	}

	/**
	 * Remove bird from the grid. (naively! does not check)
	 * @param locX : x position cleared
	 * @param locY : y position cleared
	 */
	public void removeBird(int locX, int locY){
		grid[locX][locY].setBird(null,false);
	}
	
	/**
	 * get a bird in mate mode at a location
	 * 
	 * @param x : x location to be polled
	 * @param y : y location to be polled
	 * @return null or a bird in mate mood at the location
	 */
	public synchronized Bird getMateBird(int x, int y){
		Bird bird = getBird(x, y);
		if (bird == null)
			return null;
		else
			return (bird.getMood() == BirdMood.MATE || 
				bird.getMood() == BirdMood.PLAYER ? bird : null );
	}
	
	/**
	 * Get a bird from the grid at location
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @return
	 */
	public synchronized Bird getBird(int x, int y){
		if (x < sizeX || y < sizeY || x < 0 || y< 0)
			return null;
		else {
			Cell cell = getCellAt(x,y);
			return (cell.hasBird() ? cell.getBird() : null); }	
	}

	/**
	 * Syntax sugar for array access, but 
	 * thread safe
	 * 
	 * @param locX
	 * @param locY
	 * @return grid cell
	 */
	public synchronized Cell getCellAt(int locX, int locY){
		return grid[locX][locY];
	}
}
