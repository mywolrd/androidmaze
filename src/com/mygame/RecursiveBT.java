package com.mygame;

import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class RecursiveBT extends WallCarver{

    private int OPP[];
    private int DX[];
    private int DY[];
    private HashMap<Character, Integer> _dirMap;
	private int nx, ny, cx, cy;
    
    public RecursiveBT(int size){
    	super(size);
    	
        _dirMap = new HashMap<Character, Integer>();
        
        _dirMap.put('N', 8);
        _dirMap.put('S', 4);
        _dirMap.put('W', 2);
        _dirMap.put('E', 1);
        
        DX = new int[9];
        DY = new int[9];
        OPP = new int[9];
        
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
    
        cx = 0; cy = 0; nx = 0; ny = 0;
        Carver();
    }//constructor

    private boolean foundValidNeighbor(){
    	boolean status = false;
    	
    	char[] array = {'N','S','E','W'};
    	Random rnd = new Random();
    	
    	for(int i=0; i < array.length; i++){

    		int pos = i + rnd.nextInt(array.length-i);
    		char temp = array[i];
    		array[i] = array[pos];
    		array[pos] = temp;
    	}
    	
    	for(int i=0; i < array.length; i++){
    		char dir = array[i];
    		
    		int tempx = cx + DX[_dirMap.get(dir)];
    		int tempy = cy + DY[_dirMap.get(dir)];
    		
    		if ((0 <= tempx) && (tempx < _size) && 
    				(0 <= tempy) && (tempy < _size) &&
    				(_grid[tempx][tempy] == 0)){
    			
    			nx = tempx;
    			ny = tempy;
    			
    			_grid[cx][cy] |= _dirMap.get(dir);
    			_grid[nx][ny] |= OPP[_dirMap.get(dir)];
    			
    			status = true;
    			break;
    		}
    	}
    	
    	return status;
    }
    
    public void Carver(){
    	Stack<Integer> stack = new Stack<Integer>();
    	
    	int visitedCells = 1;
    	int totalCells = _size*_size;
    	boolean status = false;
    	
    	while(visitedCells < totalCells){
    		status = foundValidNeighbor();
    		
    		if (status){
    			
    			stack.push(cx);
    			stack.push(cy);
    			cx = nx;
    			cy = ny;
    			visitedCells++;
    		}else{
    			if(!stack.empty()){
    				cy = stack.pop();
    				cx = stack.pop();
    			}
    		}
    	}
    }
    
    public boolean canImoveHorizontal(int x, int y, int z1, int z2){
    	//if the player can move in horizontal direction
    	// (x, z1) = current position, (y, z1) = next position
    	//return false 
    	boolean status = false;
    	
    	if(x < y){
    		//moving to the right
    		//check for the east wall of (x, z) cell
    		if( (_grid[x][z1] & (byte)1) != 0 ){
        		status = true;        		
        	}
    	}else{
    		//moving to the left
    		//check for the east wall of (y, z)
    		if( (_grid[y][z1] & (byte)1) != 0 ){
        		status = true;    		
    		}
    	}
    	
    	if(z1 != z2)
    		status = false;

    	return status;
    }
    
    public boolean canImoveVertical(int x1, int x2, int y, int z){
    	//if the player can move in vertical direction
    	// (x1, y) = current position, (x1, z) = next position
    	//return false 
    	boolean status = false;

    	if(y < z){
    		//moving down
    		//check for the south wall of (x, y) cell
    		if( (_grid[x1][y] & (byte)4) != 0 ){
    			status = true;
    		}
    	}else{
    		//moving up
    		//check for the south wall of (x, z) cell
    		if( (_grid[x1][z] & (byte)4) != 0 ){
    			status = true;
    		}
    	}
    	
    	if(x1 != x2)
    		status = false;
    		
    	return status;
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
