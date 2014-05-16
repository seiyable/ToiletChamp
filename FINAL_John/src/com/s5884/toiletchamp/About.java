package com.s5884.toiletchamp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
    	ActionBar actionBar = getActionBar();
    	actionBar.hide();
	
	}
}
