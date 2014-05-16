package com.s5884.toiletchamp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Ranking extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		
		
		findViewById(R.id.myactivityR).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick (View v){
				Intent intent = new Intent(Ranking.this, MyActivity.class);
				startActivity(intent);
			}
		});

		findViewById(R.id.helpR).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick (View v){
				Intent intent = new Intent(Ranking.this, HowToUse.class);
				startActivity(intent);
			}
		});
		
		// no action bar
    	ActionBar actionBar = getActionBar();
    	actionBar.hide();
	
	}
}
