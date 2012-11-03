package com.mygame;


public class WallCarver{

    protected byte _size;
	
    protected byte _grid[][];
    public WallCarver(byte size){
    	
        _size = size;
        
        _grid = new byte[_size][_size];
     
        for(int i=0; i<_size; i++){
        	for(int j=0; j<_size; j++)
        		_grid[i][j] = 0;
        }
    }//constructor


}
