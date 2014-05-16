package com.s5884.toiletchamp;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;

public class MainActivity extends Activity {

	// for screen
	private TextView addActivityTextView;
	private Button getActivityButton;
	private ListView activityListView;
	private Bitmap image;
	private MyAsyncTask_AddActivityData addActivityTask;
	private MyAsyncTask_GetActivityData getActivityTask;
	private String udid;
	
	// for iBeacon
	private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D"; // should be changed later
	private static final int ESTIMOTE_MAJOR = 37402;
	private static final int ESTIMOTE_MINOR_ER = 35485;
	private static final int ESTIMOTE_MINOR_SHOP = 25294;
	private BeaconManager beaconManager;
	private NotificationManager notificationManager;
	private Region region;

	private static final int NOTIFICATION_ID = 123;
	private static final int REQUEST_ENABLE_BT = 1234;

	// ===================== onCreate =====================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity_main);

		// get components from screen
		addActivityTextView = (TextView) findViewById(R.id.add_activity_text);
		//getActivityButton = (Button) findViewById(R.id.get_activity_button);
		activityListView = (ListView) findViewById(R.id.activity_list);
		image = BitmapFactory.decodeResource(getResources(), R.drawable.del);

		// get android_id of this phone
		udid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

		// shared preferences
		final SharedPreferences sharedPreferences = this.getSharedPreferences(
				"sharedprefsname", MODE_PRIVATE);
		final SharedPreferences.Editor spe = sharedPreferences.edit();

		// config for iBeacon
		region = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, ESTIMOTE_MAJOR, null);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		beaconManager = new BeaconManager(this);

		// set background scan period
		// Default values are 5s of scanning and 25s of waiting time to save CPU cycles.
		// In order for this demo to be more responsive and immediate we lower down those values.
		beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);

		
		
		
		//////////////////////// add activity data //////////////////////////
		beaconManager.setMonitoringListener(new MonitoringListener() {
			@Override
			public void onEnteredRegion(Region region, List<Beacon> beacons) {
				postNotification("Entered region");
				// send data to server
				// -------------------------------------------
				final long timestamp = System.currentTimeMillis() / 1000L;
				String location = "";
				if (beacons.get(0).getMinor() == ESTIMOTE_MINOR_ER){
					location = "ER";
				} else if (beacons.get(0).getMinor() == ESTIMOTE_MINOR_SHOP){
					location = "SHOP";
				}
				ActivityData_Add data = new ActivityData_Add(udid, location, timestamp);

				// only if the user exited out from the region once
				boolean onceYouExit = true;
				onceYouExit = sharedPreferences.getBoolean("onceYouExit", true);
				if (onceYouExit == true) {
					addActivityTask = new MyAsyncTask_AddActivityData(addActivityTextView);
					addActivityTask.execute(data); // start async process
					spe.putBoolean("onceYouExit", false);
					spe.commit();
				}

				// --------------------------------------------
			}

			@Override
			public void onExitedRegion(Region region) {
				// change the exit status
				postNotification("Exited region");
				spe.putBoolean("onceYouExit", true);
				spe.commit();
			}
		});


		
		
		//////////////////////// get activity data //////////////////////////		
		getActivityButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//((Button) v).setEnabled(false);
				//-------------- get a list of activity data -------------
				//final String locationFilter = "Shop";
				//final long timeFilter = System.currentTimeMillis() / 1000L;
				
				//ActivityData_Get data = new ActivityData_Get(udid, locationFilter, timeFilter);
				ActivityData_Get data = new ActivityData_Get(udid);
				getActivityTask = new MyAsyncTask_GetActivityData(MainActivity.this, activityListView, image);
				getActivityTask.execute(data); // start async process
				
				//--------------------------------------------------------
			}
		});
	}

	// ===================== onResume =====================
	@Override
	protected void onResume() {
		super.onResume();

		// Check if device supports Bluetooth Low Energy.
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy",
					Toast.LENGTH_LONG).show();
			return;
		}

		// If Bluetooth is not enabled, let user enable it.
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}
	}

	// for users coming back from bluetooth setting
	// ===================== onActivityResult =====================
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				connectToService();
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG)
						.show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ===================== connectToService =====================
	private void connectToService() {
		notificationManager.cancel(NOTIFICATION_ID);

		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startMonitoring(region);
				} catch (RemoteException e) {
					Toast.makeText(MainActivity.this,
							"Cannot start monitoring", Toast.LENGTH_LONG)
							.show();
					Log.e("connectToService", "Cannot start ranging", e);
				}
			}
		});
	}

	// ===================== postNotification =====================
	private void postNotification(String msg) {
		Intent notifyIntent = new Intent(MainActivity.this, MainActivity.class);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivities(
				MainActivity.this, 0, new Intent[] { notifyIntent },
				PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(MainActivity.this)
				.setSmallIcon(R.drawable.beacon_gray)
				.setContentTitle("Notify Demo").setContentText(msg)
				.setAutoCancel(true).setContentIntent(pendingIntent).build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notificationManager.notify(NOTIFICATION_ID, notification);

		TextView statusTextView = (TextView) findViewById(R.id.add_activity_text);
		statusTextView.setText(msg);
	}
}