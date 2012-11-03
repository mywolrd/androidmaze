package com.mygame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
	protected void onResume() {
		super.onResume();
	}
	
	/** Also pause the glSurface  */
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	public void singleplayer(){
		
		
	}
	
	public void option(){
		
		
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
