package com.fun.radarpusht;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RadarService extends AbstractService {

    private List<CameraData> cameras = new ArrayList<CameraData>();

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

		for (CameraData camera : cameras) {
			System.out.println(camera.description);
		}

		//listen to location change

//		registerLocationCallbacks();

    }

	@Override
	public void onStopService() {}
	@Override
	public void onReceiveMessage(Message msg) {

		switch(msg.what){
			case MyActivity.LOCATION_DEBUG:
				Location location = (Location)msg.obj;
				checkForCloseCameras(location);
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
                checkForCloseCameras(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void checkForCloseCameras(Location location) {
        Log.i("radar_pusht", "location is " + location.getLatitude() + ", " + location.getLongitude());
        for (CameraData cam : cameras) {
            double distance = location.distanceTo(cam.getLocation());
            int roundedDistance = ((int) (distance / 100));
            if (distance <= 500 && cam.getLastDistanceMessage() > roundedDistance) {
                String alert = "in " + ((int) distance) + " meters " + cam.descriptionEn;
                Notification.notifyPebble(getApplicationContext(), "PebbleCam","in " + ((int) distance) + " meters", cam.descriptionEn);
                Indicator.showIndicator(this,alert,alert);
                cam.setLastDistanceMessage(roundedDistance);
//                Log.i("rwqadar_pusht", location.getLatitude() + "_" + location.getLongitude() + " ," + cam.description + " ," + distance + " meters, " + roundedDistance);

            }
        }
    }


}
