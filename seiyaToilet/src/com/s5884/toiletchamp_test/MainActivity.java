package com.s5884.toiletchamp_test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.BeaconManager.MonitoringListener;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;

public class MainActivity extends Activity {

	//for iBeacon
	private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D"; // should be changed later
	private BeaconManager beaconManager;
    private NotificationManager notificationManager;
    private Region region;
	
	private static final String TAG = NotifyDemoActivity.class.getSimpleName();
	private static final int NOTIFICATION_ID = 123;
	private static final int REQUEST_ENABLE_BT = 1234;
	
	//for screen
	private Button addActivityButton;
	private TextView addActivityTextView;
	private MyAsyncTask task;
	private String udid;

	//===================== onCreate =====================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//get components
		addActivityButton = (Button) findViewById(R.id.add_activity_button);
		addActivityTextView = (TextView) findViewById(R.id.add_activity_text);

		//get android_id of this phone
		udid = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
		
		//shared preferences
	    final SharedPreferences sharedPreferences = this.getSharedPreferences("sharedprefsname", MODE_PRIVATE);
	    final SharedPreferences.Editor spe = sharedPreferences.edit();
		
		//config for iBeacon
	    //Beacon beacon = getIntent().getParcelableExtra(ListBeaconsActivity.EXTRAS_BEACON);
	    region = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
	    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    beaconManager = new BeaconManager(this);

	    //set background scan period 
	    // Default values are 5s of scanning and 25s of waiting time to save CPU cycles.
	    // In order for this demo to be more responsive and immediate we lower down those values.
	    beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);

	    //set monitoring listener
	    beaconManager.setMonitoringListener(new MonitoringListener() {
	      @Override
	      public void onEnteredRegion(Region region, List<Beacon> beacons) {
	        postNotification("Entered region");
	        // send data to server -------------------------------------------
			final String location = "shop";
			final long timestamp = System.currentTimeMillis() / 1000L;
		    ActivityData data = new ActivityData(udid, location, timestamp);
		    
		    // only if the user exited out from the region once
		    boolean onceYouExit = true;
		    onceYouExit= sharedPreferences.getBoolean("onceYouExit", true);
		    if (onceYouExit){
		    	Log.v("onceYouExit", "true");
		    } else {
		    	Log.v("onceYouExit", "false");
		    }
		    if (onceYouExit == true){
		    	task = new MyAsyncTask(addActivityTextView);
				task.execute(data);  //start async process
		        spe.putBoolean("onceYouExit", false);
		        spe.commit();				
		    }
	        
	        //----------------------------------------------------------------
	      }

	      @Override
	      public void onExitedRegion(Region region) {
	        postNotification("Exited region");
	        spe.putBoolean("onceYouExit", true);
	        spe.commit();
	      }
	    });
		
		//register click listener
		addActivityButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Button)v).setEnabled(false);
				
				//final String udid = "JIJIJDLFHALSIFHA";
				//final int timestamp = "1600";

				final String location = "ER";
				final long timestamp = System.currentTimeMillis() / 1000L;
			    ActivityData data = new ActivityData(udid, location, timestamp);
			    
				task = new MyAsyncTask(addActivityTextView);
				task.execute(data);  //start async process
			}
		});
	}
	
	
	//===================== onResume =====================
	@Override
	protected void onResume() {
	  super.onResume();
	  
	  // Check if device supports Bluetooth Low Energy.
	  if (!beaconManager.hasBluetooth()) {
	    Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
	    return;
	  }

	  // If Bluetooth is not enabled, let user enable it.
	  if (!beaconManager.isBluetoothEnabled()) {
	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	  } else {
	    connectToService();
	  }
	}

	//for users coming back from bluetooth setting
	//===================== onActivityResult =====================		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (requestCode == REQUEST_ENABLE_BT) {
	    if (resultCode == Activity.RESULT_OK) {
	      connectToService();
	    } else {
	      Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
	      getActionBar().setSubtitle("Bluetooth not enabled");
	    }
	  }
	  super.onActivityResult(requestCode, resultCode, data);
	}
	
	//===================== connectToService =====================	
	private void connectToService() {
		notificationManager.cancel(NOTIFICATION_ID);

		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
	    @Override
	    public void onServiceReady() {
		    try {
		      beaconManager.startMonitoring(region);
		    } catch (RemoteException e) {
		      Toast.makeText(MainActivity.this, "Cannot start monitoring",
		          Toast.LENGTH_LONG).show();
		      Log.e(TAG, "Cannot start ranging", e);
		    }
		  }
		});
	}	
		
	//===================== postNotification =====================
	private void postNotification(String msg) {
	  Intent notifyIntent = new Intent(MainActivity.this, MainActivity.class);
	  notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	  PendingIntent pendingIntent = PendingIntent.getActivities(
	      MainActivity.this,
	      0,
	      new Intent[]{notifyIntent},
	      PendingIntent.FLAG_UPDATE_CURRENT);
	  Notification notification = new Notification.Builder(MainActivity.this)
	      .setSmallIcon(R.drawable.beacon_gray)
	      .setContentTitle("Notify Demo")
	      .setContentText(msg)
	      .setAutoCancel(true)
	      .setContentIntent(pendingIntent)
	      .build();
	  notification.defaults |= Notification.DEFAULT_SOUND;
	  notification.defaults |= Notification.DEFAULT_LIGHTS;
	  notificationManager.notify(NOTIFICATION_ID, notification);

	  TextView statusTextView = (TextView) findViewById(R.id.add_activity_text);
	  statusTextView.setText(msg);
	}
}