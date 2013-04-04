package com.mygame;

public class Player {
	
	private int type;
	private float xp,yp;	// position
	private float xs, ys;	// speed - x and y have different speed because of 
							//	difference in height and width of a screen
	
	private int speedIndex;
	
	private int direction;	
	private int speed[];
	
	 public Player(){
		
		this.direction = 0;
		
		setSpeedArray();
	 }
	
    public Player(float x, float y){

    	this.direction = 0;

		this.xp = x;
		this.yp = y;
		
		setSpeedArray();
	}
	
	public Player(float x, float y, int type){

		this.direction = 0;

		this.type = type;
		this.xp = x;
		this.yp = y;
		
		setSpeedArray();
	}
	
	private void setSpeedArray(){
		speed = new int[6];
		
		this.speedIndex = 0;
		
		for (int i = 1; i<= 6; i++){
			speed[i-1] = 2 * i;
		}
	}
	
	public void increaseSpeed(){
		if(speedIndex < 5)
			speedIndex++;
	}
	
	public void decreaseSpeed(){
		if(speedIndex > 0)
			speedIndex--;
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
		return xs*speed[speedIndex];
	}
	
	public float getYSpd(){
		return ys*speed[speedIndex];		
	}
}
