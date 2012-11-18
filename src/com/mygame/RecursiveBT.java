package com.mygame;

import java.util.HashMap;
import java.util.Random;

public class RecursiveBT extends WallCarver{

    private byte OPP[];
    private byte DX[];
    private byte DY[];
    
    private HashMap<Character, Byte> _dirMap;
	
    public RecursiveBT(byte size){
    	super(size);
    	
        _dirMap = new HashMap<Character, Byte>();
        
        _dirMap.put('N', (byte)8);
        _dirMap.put('S', (byte)4);
        _dirMap.put('W', (byte)2);
        _dirMap.put('E', (byte)1);
        
        DX = new byte[9];
        DY = new byte[9];
        OPP = new byte[9];
        
        DX[8] = 0;
        DX[4] = 0;
        DX[2] = -1;
        DX[1] = 1;
        
        DY[8] = -1;
        DY[4] = 1;
        DY[2] = 0;
        DY[1] = 0;
        
        OPP[8] = 4;
        OPP[4] = 8;
        OPP[2] = 1;
        OPP[1] = 2;
    
        Carver((byte)0, (byte)0);
    }//constructor

    public void Carver(byte cx, byte cy){

    	byte nx, ny;
    	
    	char[] direction; 
    	
    	direction = shuffle();
    	
    	for(int i=0; i<4; i++){
    		
    		nx = (byte)(cx + DX[_dirMap.get(direction[i])]);
    		ny = (byte)(cy + DY[_dirMap.get(direction[i])]);
    	
    		if((nx >= (byte)0) && (nx <_size) 
    			&& (ny >= (byte)0) && (ny < _size) 
    			&& (_grid[nx][ny] == 0)){

    			_grid[cx][cy] |= _dirMap.get(direction[i]);
    			_grid[nx][ny] |= OPP[_dirMap.get(direction[i])];
    			Carver(nx, ny);
    		}
    	}
    }

    private char[] shuffle(){
    	
    	char[] array = {'N','S','E','W'};
    	Random rnd = new Random();
    	
    	for(int i=0; i < array.length; i++){

    		int pos = i + rnd.nextInt(array.length-i);
    		char temp = array[i];
    		array[i] = array[pos];
    		array[pos] = temp;
    	}
    	return array;
    }
    
    public int getWalls(int x, int y){
    	//for a cell at (x, y)
    	//returns 0 if the cell has no east and south wall
    	//        1 if the cell has only east wall
    	//        2 if the cell has only south wall
    	//        3 if the cell has both east and south wall
    	
    	int wall = 0;
    	
    	if( (_grid[x][y] & (byte)1) == 0 ){
    		wall = 1;
    	}
    	
    	if( (_grid[x][y] & (byte)4) == 0 ){
    		if(wall == 1){
    			wall = 3;
    		}
    		else{
    			wall = 2;
    		}
    	}
    	
    	return wall;
    }
}
