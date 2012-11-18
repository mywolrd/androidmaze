package com.mygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
     	requestWindowFeature(Window.FEATURE_NO_TITLE);
     		
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
	
	public void singlePlayer(View view){
		Intent intent = new Intent(this, MazeActivity.class);
		startActivity(intent);
	}
	
	public void option(){
		
		
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
