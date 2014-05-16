package com.s5884.toiletchamp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Signup extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		findViewById(R.id.createID).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick (View v){
				Intent intent = new Intent(Signup.this, Setting.class);
				startActivity(intent);
			}
		});
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();

	}
	

}
