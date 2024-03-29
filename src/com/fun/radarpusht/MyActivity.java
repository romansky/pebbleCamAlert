package com.fun.radarpusht;

import android.app.Activity;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Queue;

public class MyActivity extends Activity {

	public final static int LOCATION_DEBUG = 1;
	public final static int NEAREST_CAMERA_CHANGE =2;

	private ServiceManager serviceManager;

	private Queue<Location> fakeLocations;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        Typeface tf = Typeface.createFromAsset(getAssets(),"raleway_thin_0.ttf");
        ((TextView) findViewById(R.id.welcome)).setTypeface(tf);
        ((Button) findViewById(R.id.btn)).setTypeface(tf);

		new TermsScreen(this).show(false);

		this.serviceManager = new ServiceManager(this, RadarService.class, new Handler(){
			@Override
			public void handleMessage(Message msg) {

				switch(msg.what){
					case NEAREST_CAMERA_CHANGE:
						String nearestCameraText = (String)msg.obj;
						nearestCameraMessage(nearestCameraText);
				}

				super.handleMessage(msg);
			}
		});

		if (this.serviceManager.isRunning()){
			((Button)findViewById(R.id.btn)).setText("Stop Service");
			findViewById(R.id.fakeLocationKey).setVisibility(View.VISIBLE);
			findViewById(R.id.nearest_camera_textview).setVisibility(View.VISIBLE);
		} else {
			((Button)findViewById(R.id.btn)).setText("Start Service");
			findViewById(R.id.fakeLocationKey).setVisibility(View.GONE);
			findViewById(R.id.nearest_camera_textview).setVisibility(View.GONE);
		}

		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startStopButtonPressed();
			}
		});


		this.fakeLocations = new ArrayDeque<Location>();
		fakeLocations.add(createLocation(31.998634,34.823002));
		fakeLocations.add(createLocation(31.999981,34.822294));
		fakeLocations.add(createLocation(32.001327,34.821285));
		fakeLocations.add(createLocation(32.002801,34.820234));
		fakeLocations.add(createLocation(32.003984,34.819268));
		fakeLocations.add(createLocation(32.005203,34.818345));
		fakeLocations.add(createLocation(32.006186,34.817594));
		fakeLocations.add(createLocation(32.007077,34.816843));
		fakeLocations.add(createLocation(32.007823,34.816328));
		fakeLocations.add(createLocation(32.00871,34.81552));

		findViewById(R.id.fakeLocationKey).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(MyActivity.class.getSimpleName(),"clicked!");
				if (!fakeLocations.isEmpty()){
					Location newLocation = fakeLocations.remove();
					try {
						Log.i(MyActivity.class.getSimpleName(),"sending service location "
								+ newLocation.getLatitude() + " " + newLocation.getLongitude());
						serviceManager.send(Message.obtain(null, LOCATION_DEBUG, newLocation));
					} catch (RemoteException e) {
						Log.i(MyActivity.class.getSimpleName(), "Exception while sending location to service",e);
					}
				}
			}
		});

	}

	private void nearestCameraMessage(String message) {
		((TextView)findViewById(R.id.nearest_camera_textview)).setText( getResources().getString(R.string.nearest_camera) + message );
	}


	private Location createLocation(Double lat, Double lon){
		Location location = new Location("data");
		location.setLongitude(lon);
		location.setLatitude(lat);
		return location;
	}


	public void startStopButtonPressed() {

		if (this.serviceManager.isRunning()){
			Toast.makeText(this, "Stopping background service..", Toast.LENGTH_SHORT).show();
			Indicator.hideServiceIndicator(this);
			this.serviceManager.stop();
			((Button)findViewById(R.id.btn)).setText("Start Service");
			findViewById(R.id.fakeLocationKey).setVisibility(View.GONE);
			findViewById(R.id.nearest_camera_textview).setVisibility(View.GONE);
		} else {
			Toast.makeText(this, "Starting background service..", Toast.LENGTH_SHORT).show();
			Indicator.showServiceIndicator(this);
			this.serviceManager.start();
			((Button)findViewById(R.id.btn)).setText("Stop Service");
			findViewById(R.id.nearest_camera_textview).setVisibility(View.VISIBLE);
//			findViewById(R.id.fakeLocationKey).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(getString(R.string.EULA))
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		// turned off for now, since were not doing real updates
//		menu.add(getString(R.string.update_cams_text))
//				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(getResources().getString(R.string.update_cams_text))){
			return true;
		} else if (item.getTitle().equals(getResources().getString(R.string.EULA))){
			new TermsScreen(this).show(true);
		}
		return super.onOptionsItemSelected(item);
	}

}
