package com.tweeter.app;

public class Cell {
	private int locationX;
	private int locationY;
	protected Cell[] neighbours;
	private boolean hasBird;
	private Bird cellBird;
	
	public Cell(int locX, int locY){
		this.locationX = locX;
		this.locationY = locY;
	}
	
	public void setHasBird(boolean b){
		this.hasBird = b;
	}
	
	public boolean hasBird(){
		return this.hasBird;
	}
	
	public int getLocX(){
		return locationX;
	}
	
	public int getLocY(){
		return locationY;
	}
	
	public Bird getBird(){
		if(this.hasBird){
			return cellBird;
		}
		return null;
	}
	
	public void setNeighbors(Cell[] n){
		this.neighbours = n;
	}
	
	public void setBird(Bird b, boolean hasB){
		this.cellBird = b;
		this.hasBird = hasB;
	}
	
	public String toString(){
		return "LocX: " + locationX + " LocY: " + locationY + " hasBird: " + hasBird;
	}
}
