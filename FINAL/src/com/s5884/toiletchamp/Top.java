package com.s5884.toiletchamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class Top extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);

		
		findViewById(R.id.signup).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick (View v){
				Intent intent = new Intent(Top.this, Signup.class);
				startActivity(intent);
			}
		});

		findViewById(R.id.link_to_about).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick (View v){
				Intent intent = new Intent(Top.this, About.class);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.link_to_howtouse).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick (View v){
				Intent intent = new Intent(Top.this, HowToUse.class);
				startActivity(intent);
			}
		});
		
	
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.top, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
