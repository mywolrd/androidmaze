package com.mygame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView implements SensorEventListener{
	
	private MyGame myGame;
	
	private int x_coord;
	private int y_coord;
	private int input;
	
	private JoystickMovedListener joystick;
	
	public MyGLSurfaceView(Context context){
		
		super(context);
		init(context);
	}
	
	public MyGLSurfaceView(Context context, AttributeSet atts){
		
		super(context, atts);
		init(context);
	}
	
	private void init(Context context){
		setEGLContextClientVersion(2);
		
		myGame = new MyGame(context);
		setRenderer(myGame);
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	
		x_coord = Integer.MIN_VALUE;
		y_coord = Integer.MIN_VALUE;
		input = 0;
		
		joystick = new JoystickMovedListener(){
				public void OnMoved(int pan, int tilt) {
					//             negative tilt  
					//                  |        
					// negative pan  ---|---   positive pan    
					//                  |            
					//             positive tilt 
					//

					//Log.e("Moved", pan + "   " + tilt);
					int x = Math.abs(pan);
					int y = Math.abs(tilt);
										
					if( x > y ){
						// move in horizontal direction
						if(pan < 0){
							myGame.input = 1;
						}else if(pan > 0){
							myGame.input = 2;
						}
						
					}else if(y > x){
						//move in vertical direction
						if(tilt < 0){
							myGame.input = 3;
						}else if(tilt > 0){
							myGame.input = 4;
						}
					}
					//requestRender(); 
				}
		    	
		    	public void OnReleased() {
		    		//Log.e("Released", "I'M IN THE JOYSTICK LISTENER");
		    		myGame.input = 0;
		    		//requestRender();
		    	}
			};
	}
	
	public JoystickMovedListener getJoystickListener(){
		return joystick;
	}
	
	public boolean onTouchEvent(MotionEvent e) {
		/*
		//http://stackoverflow.com/questions/7545591/motionevent-issues/7577139#7577139
		//stackoverflow "Knickedi"
		Log.d("HERE", "I'M IN THE GLSURFACEVIEWTOUCHEVENT");
		if (e.getAction() == MotionEvent.ACTION_CANCEL) {

			// every touch is going to be canceled, clear everything
			x_coord = Integer.MIN_VALUE;
			y_coord = Integer.MIN_VALUE;
		} else {
        	
            // track all touches
            for (int i = 0; i < e.getPointerCount(); i++) {
                
            	int id = e.getPointerId(0);

                if (e.getActionIndex() == 0
                	&& (e.getActionMasked() == MotionEvent.ACTION_POINTER_UP
                    || e.getActionMasked() == MotionEvent.ACTION_UP)) {
                    
                	// pointer with this id is about to leave, clear it
                    x_coord = y_coord = Integer.MIN_VALUE;
                
                } else {
                
                	// update all other pointer positions
                	x_coord = (int) e.getX(i);
                    y_coord = (int) e.getY(i);
                }
            }
        }
		
		int width = getWidth() / 2;
        int height = getHeight() / 2;

        // first no corner is touched
        boolean topLeft = false, topRight = false, bottomRight = false, bottomLeft = false;
        input = 0;
        // check if any of the given pointer is touching a certain corner    
        // pointer is not active (anymore)
        if (x_coord != Integer.MIN_VALUE && y_coord != Integer.MIN_VALUE) {
        
        	topLeft = topLeft || x_coord < width && y_coord < height;
            input += 1 * (topLeft ? 1:0);
            
            topRight = topRight || x_coord > width && y_coord < height;
            input += 2 * (topRight ? 1:0);
            
            bottomRight = bottomRight || x_coord > width && y_coord > height;
            input += 3 * (bottomRight ? 1:0);
            
            bottomLeft = bottomLeft || x_coord < width && y_coord > height;
            input += 4 * (bottomLeft ? 1:0);
        }
        
        myGame.input = input;
        
        //requestRender();
        */
	    return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
