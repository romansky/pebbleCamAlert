package com.fun.radarpusht;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RadarService extends AbstractService {

    private List<CameraData> cameras = new ArrayList<CameraData>();

	private final int ALERT_DISTANCE = 500;

	private int closestCameraDistance = 0;

	@Override
	public void onStartService() {
		Log.i(RadarService.class.getSimpleName(), "onStartService");
		new AsyncTask<Void,Void,Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				cameras = CamsKmzFileParser.parseKMZ(RadarService.this);
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				Toast.makeText(RadarService.this, "Finished loading camera data", Toast.LENGTH_SHORT).show();
			}
		}.execute();

		//listen to location change
		registerLocationCallbacks();

    }

	@Override
	public void onStopService() {}
	@Override
	public void onReceiveMessage(Message msg) {

		switch(msg.what){
			case MyActivity.LOCATION_DEBUG:
				Location location = (Location)msg.obj;
				updateMyLocation(location);
		}
	}

    private void registerLocationCallbacks() {
        Log.i("radar_pusht", "registerLocationCallbacks");
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
				updateMyLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

	private CameraData findClosestCamera(Location location){
		CameraData closestCamera = null;
		for (CameraData cam : cameras) {
			if (closestCamera == null){
				closestCamera = cam;
			} else {
				double distance = location.distanceTo(cam.getLocation());
				double closeCamDistance = location.distanceTo(closestCamera.getLocation());
				if (closeCamDistance > distance){
					closestCamera = cam;
				}
			}
		}
		return closestCamera;
	}

	private void updateMyLocation(Location location){
		CameraData closestCamera = findClosestCamera(location);
		if (closestCamera != null){
			updateNearestCamera(closestCamera,location);
			int roundedDistance = roundedDistanceMeter(closestCamera, location);
			if (roundedDistance <= ALERT_DISTANCE && closestCameraDistance > roundedDistance){
				notifyCaneraInRange(closestCamera,location);
			}
			closestCameraDistance = roundedDistance;
		}
	}

	private void notifyCaneraInRange(CameraData closestCamera,Location location) {
		int distanceToCam = roundedDistanceMeter(closestCamera,location);
		String messageText = closestCamera.descriptionEn + " (" + distanceToCam + "m)";
		// notify pebble
		Notification.notifyPebble(getApplicationContext(), "PebbleCam","in " + ((int) distanceToCam) + "m", messageText);
		// notify phone
		Indicator.showIndicator(this,messageText,messageText);
	}

	private void updateNearestCamera(CameraData closestCamera,Location location) {
		String messageText = closestCamera.descriptionEn + " (" + roundedDistanceMeter(closestCamera,location) + "m)";
		this.send( Message.obtain(null, MyActivity.NEAREST_CAMERA_CHANGE, messageText) );
	}

	private int roundedDistanceMeter(CameraData camera, Location location){
		return (int)(location.distanceTo(camera.getLocation()));
	}

}
