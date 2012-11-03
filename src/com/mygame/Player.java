package com.mygame;

public class Player {
	
	public static final float X_INIT = -1.0f;
	public static final float Y_INIT = 1.5f;
	private float x,y;
	private float x_inc, y_inc;
	
	public Player(){
		this.x = X_INIT;
		this.y = Y_INIT;
	}
	
	public Player(float x, float y, float x_inc, float y_inc ){
		this.x = X_INIT;
		this.y = Y_INIT;
		this.x_inc = x_inc;
		this.y_inc = y_inc;
	}
	
	public void move(float x, float y){
		
	}
	
	public float[] getPosition(){
		float [] pos = new float[2];
		pos[0] = this.x;
		pos[1] = this.y;
		return pos;
	}

	 
}
