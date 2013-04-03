package com.mygame;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MazeActivity extends Activity{
	 /** The OpenGL view */
	private MyGLSurfaceView glSurfaceView;

	private JoystickView joystick;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		// Initiate the Open GL view and
		// create an instance with this activity
		
		setContentView(R.layout.activity_maze);
		
		glSurfaceView = (MyGLSurfaceView) findViewById(R.id.myGLSurfaceView);

		joystick = (JoystickView) findViewById(R.id.joystickView);
		
		JoystickMovedListener myListener = new JoystickMovedListener(){
			public void OnMoved(int pan, int tilt) {
				Log.d("HERE", "I'M IN THE JOYSTICK LISTENER");
	    	}
	    	
	    	public void OnReleased() {
	    		Log.d("HERE", "I'M IN THE JOYSTICK LISTENER");
	    	}
		};
		joystick.setOnJostickMovedListener(myListener);
	}

	/** Remember to resume the glSurface  */
	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
	}
	
	/** Also pause the glSurface  */
	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
	}

	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

	    }
	  }
}
