package com.mygame;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MazeActivity extends Activity implements SensorEventListener {
	 /** The OpenGL view */
	private MyGLSurfaceView glSurfaceView;
	private SensorManager sensorManager;

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
		
		glSurfaceView = new MyGLSurfaceView(this);
		setContentView(glSurfaceView);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	/** Remember to resume the glSurface  */
	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
		sensorManager.registerListener(this,
		        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		        SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	/** Also pause the glSurface  */
	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
		sensorManager.unregisterListener(this);
	}
	
	public void onSensorChanged(SensorEvent event) {
	    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	      update(event);
	    }
	}
	
	private void update(SensorEvent event) {
	    
		float[] values = event.values;
	    // Movement
	    
		float x = values[0];
	    float y = values[1];
	    float z = values[2];

	    float accelationSquareRoot = (x * x + y * y + z * z)
	        / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
	    
	    long actualTime = System.currentTimeMillis();
	    long lastUpdate = 0;
	    
	    if (accelationSquareRoot >= 2){
	    	
	    	if (actualTime - lastUpdate < 200) {
	    		return;
	    	}
	      lastUpdate = actualTime;
	     
	    }
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

	    }
	  }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
