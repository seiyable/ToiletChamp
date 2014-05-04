package com.s5884.toiletchamp2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LodingPage extends Activity {
	
	// screen timer
    private static int TIME_OUT = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadingpage);
		
        new Handler().postDelayed(new Runnable() {
        	 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(LodingPage.this, Top.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, TIME_OUT);
	}
	


 
 
}
