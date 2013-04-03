package com.mygame;


public class WallCarver{

    protected int _size;
	
    protected int _grid[][];
    public WallCarver(int size){
    	
        _size = size;
        
        _grid = new int[_size][_size];
     
        for(int i=0; i<_size; i++){
        	for(int j=0; j<_size; j++)
        		_grid[i][j] = 0;
        }
    }//constructor


}
