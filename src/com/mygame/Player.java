package com.mygame;

public class Player {
	
	private int type;
	private float xp,yp;	// position
	private float xs, ys;	// speed - x and y have different speed because of 
							//	difference in height and width of a screen
	
	private int mult;
	private int direction;
	
	private final int speed1 = 1;
	private final int speed2 = 2;
	private final int speed3 = 3;
	private final int speed4 = 4;
	
	 public Player(){
		this.mult = speed2;
		this.direction = 0;
	 }
	
    public Player(float x, float y){

		this.mult = speed2;
		this.xp = x;
		this.yp = y;
		this.direction = 0;
	}
	
	public Player(float x, float y, int type){
		
		this.type = type;
		this.mult = speed1;
		this.xp = x;
		this.yp = y;
		this.direction = 0;
	}
	
	public void setPosition(float x, float y){
		// x = x coordinate of top left corner
		// y = y coordinate of top left corner
		this.xp = x;
		this.yp = y;
	}
	
	public void setXPos(float x){
		this.xp = x;
	}
	
	public void setYPos(float y){
		this.yp = y;
	}
	
	public void setSpeed(float x, float y){
		this.xs = x;
		this.ys = y;
	}
	
	public void move(float x, float y){
		
	}

	public float getXPos(){
		return xp;
	}
	
	public float getYPos(){
		return yp;
	}
	
	public float getXSpd(){
		return xs*mult;
	}
	
	public float getYSpd(){
		return ys*mult;		
	}
}
