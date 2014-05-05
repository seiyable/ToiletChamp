package com.s5884.toiletchamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Setting extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		
		findViewById(R.id.done).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick (View v){
				Intent intent = new Intent(Setting.this, MyActivity.class);
				startActivity(intent);
			}
		});
	
	
	}
	
	
	
	
}
