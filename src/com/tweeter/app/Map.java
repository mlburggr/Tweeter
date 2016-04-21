package com.tweeter.app;

import java.util.ArrayList;
import java.util.List;

public class Map {
	public int sizeX;
	public int getSizeX() {
		return sizeX;
	}

	public int sizeY;

	private Cell[][] grid;
	
	public Map(int sX, int sY){
		this.sizeX = sX;
		this.sizeY = sY;
		grid = new Cell[sX][sY];
	}
	
	public void clear(){
		
	}
	public void setNeighbors(){
		for(int j = 0; j < sizeX-1; j++){
			for(int k = 0; k < sizeY-1; k++){
				List<Cell> neighbors = new ArrayList<Cell>();
				
				for (int dx = (j > 0 ? -1 : 0); dx <= (j < sizeX-1 ? 1 : 0); dx++){
				    for (int dy = (k > 0 ? -1 : 0); dy <= (k < sizeY-1 ? 1 : 0); dy++){
				        if (dx != 0 || dy != 0){
				            neighbors.add(new Cell(j + dx, k + dy));
				        }
				    }
				}
				
				Cell[] n = neighbors.toArray(new Cell[neighbors.size()]);
				
				grid[j][k].setNeighbors(n);
			}
		}
	}
	public Cell[][] getGrid(){
		return grid;
	}
	
	public void addToGrid(Cell c, int locX, int locY){
		grid[locX][locY] = c;
	}
	
	public void addBird(Bird b){
		grid[b.getPosX()][b.getPosY()].setHasBird(true);
		grid[b.getPosX()][b.getPosY()].setBird(b,true);
	}
	
	public void removeBird(int locX, int locY){
		grid[locX][locY].setHasBird(false);
		grid[locX][locY].setBird(null,false);
		System.out.println("Bird nulled at "+ locX + " " + locY);
	}
	
	public boolean cellHasBird(int locX, int locY){
		return grid[locX][locY].hasBird();
	}
	
	public Cell getCellAt(int locX, int locY){
		return grid[locX][locY];
	}
	
	public void moveUp(Bird b, int origX, int origY){
		if(origY - 1 < 0){
			return;
		}
		if(grid[origX][origY-1].hasBird()){
			
			// bird wants to go to the same spot
			// thus, they mate and make new bird
			Bird partner = grid[origX][origY-1].getBird();
			if (b.mood == BirdMood.MATE && partner.mood == BirdMood.MATE) {
				b.mate(partner,this);
			}
			
			else if (b.mood == BirdMood.ATTACK && partner.mood == BirdMood.ATTACK) {
				b.attack(partner,this);
			}
			
			return;
		}
		Cell cNew = new Cell(origX,origY);
		grid[origX][origY-1] = cNew;
		b.setPosY(origY-1);
		addBird(b);
		removeBird(origX, origY);
		
		//////////////////////////////////////////////////////////////////////////////
		Cell[] adjacents = grid[origX][origY-1].neighbours;
		if(adjacents != null){
			findBirdLoop:
				for (Cell c : adjacents) {
					if (c!=null) {
						if (c.hasBird()) {
							Bird adj = c.getBird();
							if (b.mood == BirdMood.MATE && adj.mood == BirdMood.MATE) {
								b.mate(adj,this);
								break findBirdLoop;
							}
						
							else if (b.mood == BirdMood.ATTACK && adj.mood == BirdMood.ATTACK) {
								b.attack(adj,this);
								break findBirdLoop;
							}
						}
					}
				}
		} else {
			setNeighbors();
		
		}
		///////////////////////////////////////////////////////////////////////////////
		
	}
	
	public void moveDown(Bird b, int origX, int origY){
		if(origY + 1 > sizeY-1){
			return;
		}
		if(grid[origX][origY+1].hasBird()){
			Bird partner = grid[origX][origY+1].getBird();
			if (b.mood == BirdMood.MATE && partner.mood == BirdMood.MATE) {
				b.mate(partner,this);
			}
			
			else if (b.mood == BirdMood.ATTACK && partner.mood == BirdMood.ATTACK) {
				b.attack(partner,this);
			}
			
			return;
		}
		Cell cNew = new Cell(origX,origY);
		grid[origX][origY+1] = cNew;
		b.setPosY(origY+1);
		addBird(b);
		removeBird(origX, origY);
		
		//////////////////////////////////////////////////////////////////////////////
		Cell[] adjacents = grid[origX][origY+1].neighbours;
		if(adjacents != null){
			findBirdLoop:
				for (Cell c : adjacents) {
					if (c!=null) {
						if (c.hasBird()) {
							Bird adj = c.getBird();
							if (b.mood == BirdMood.MATE && adj.mood == BirdMood.MATE) {
								b.mate(adj,this);
								break findBirdLoop;
							}
						
							else if (b.mood == BirdMood.ATTACK && adj.mood == BirdMood.ATTACK) {
								b.attack(adj,this);
								break findBirdLoop;
							}
						}
					}
				}
		} else {
			setNeighbors();
		
		}
		///////////////////////////////////////////////////////////////////////////////
		
	}
	
	public void moveLeft(Bird b, int origX, int origY){
		if(origX - 1 < 0){
			return;
		}
		if(grid[origX-1][origY].hasBird()){
			Bird partner = grid[origX-1][origY].getBird();
			if (b.mood == BirdMood.MATE && partner.mood == BirdMood.MATE) {
				b.mate(partner,this);
			}
			
			else if (b.mood == BirdMood.ATTACK && partner.mood == BirdMood.ATTACK) {
				b.attack(partner,this);
			}
			
			return;
		}
		Cell cNew = new Cell(origX,origY);
		grid[origX-1][origY] = cNew;
		b.setPosX(origX-1);
		addBird(b);
		removeBird(origX, origY);
		
		//////////////////////////////////////////////////////////////////////////////
		Cell[] adjacents = grid[origX-1][origY].neighbours;
		if(adjacents != null){
			findBirdLoop:
				for (Cell c : adjacents) {
					if (c!=null) {
						if (c.hasBird()) {
							Bird adj = c.getBird();
							if (b.mood == BirdMood.MATE && adj.mood == BirdMood.MATE) {
								b.mate(adj,this);
								break findBirdLoop;
							}
						
							else if (b.mood == BirdMood.ATTACK && adj.mood == BirdMood.ATTACK) {
								b.attack(adj,this);
								break findBirdLoop;
							}
						}
					}
				}
		} else {
			setNeighbors();
		
		}
		///////////////////////////////////////////////////////////////////////////////
		
	}
	
	public void moveRight(Bird b, int origX, int origY){
		if(origX + 1 > sizeX-1){
			return;
		}
		if(grid[origX+1][origY].hasBird()){
			Bird partner = grid[origX+1][origY].getBird();
			if (b.mood == BirdMood.MATE && partner.mood == BirdMood.MATE) {
				b.mate(partner,this);
			}
			
			else if (b.mood == BirdMood.ATTACK && partner.mood == BirdMood.ATTACK) {
				b.attack(partner,this);
			}
			
			return;
		}
		
		Cell cNew = new Cell(origX,origY);
		grid[origX+1][origY] = cNew;
		b.setPosX(origX+1);
		addBird(b);
		removeBird(origX, origY);
		
		//////////////////////////////////////////////////////////////////////////////
		Cell[] adjacents = grid[origX+1][origY].neighbours;
		if(adjacents != null){
			findBirdLoop:
				for (Cell c : adjacents) {
					if (c!=null) {
						if (c.hasBird()) {
							Bird adj = c.getBird();
							if (b.mood == BirdMood.MATE && adj.mood == BirdMood.MATE) {
								b.mate(adj,this);
								break findBirdLoop;
							}
						
							else if (b.mood == BirdMood.ATTACK && adj.mood == BirdMood.ATTACK) {
								b.attack(adj,this);
								break findBirdLoop;
							}
						}
					}
				}
		} else {
			setNeighbors();
		
		}
		///////////////////////////////////////////////////////////////////////////////
		
		//System.out.println(cellHasBird(origX,origY) + " " + cellHasBird(origX+1,origY));
	}
}
