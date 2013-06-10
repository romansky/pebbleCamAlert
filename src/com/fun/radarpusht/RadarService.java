package com.fun.radarpusht;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
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

/**
 * Created by IntelliJ IDEA.
 * User: romansky
 * Date: 2/16/12
 * Time: 12:02 AM
 */
public class RadarService extends AbstractService {

    private List<CameraData> cameras = new ArrayList<CameraData>();



	@Override
	public void onStartService() {
        Log.i("radar_pusht", "onStartCommand");

        InputStream fis;
        AssetManager assetManager = getAssets();
        try {
            fis = assetManager.open("english_names.txt");
            DataInputStream dataIO = new DataInputStream(fis);
            String strLine = null;

		//load cameras data
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document camerasDoc = builder.parse(getAssets().open("gatso_speed_camera_01_2012.kml"));
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xpath.evaluate("//Placemark", camerasDoc, XPathConstants.NODESET);

			for (Integer i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				CameraData camD = new CameraData(
						xpath.evaluate("name/text()", node, XPathConstants.STRING).toString(),
//						xpath.evaluate("description/text()", node),
                        dataIO.readLine(),
						xpath.evaluate("Point/coordinates/text()", node).split(",")[0],
						xpath.evaluate("Point/coordinates/text()", node).split(",")[1]
				);
				cameras.add(camD);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		//listen to location change
		registerLocationCallbacks();




            dataIO.close();
            fis.close();
        }
        catch  (Exception e) {
        }

    }

	@Override
	public void onStopService() {}
	@Override
	public void onReceiveMessage(Message msg) {

		Log.i(RadarService.class.getSimpleName(),"service got message");
		Log.i(RadarService.class.getSimpleName(),">>>" + ((Location)msg.obj).getLongitude() );

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
                String alert = "in " + ((int) distance) + " meters " + cam.description;
                Notification.notifyPebble(getApplicationContext(), "RadarPusht","in " + ((int) distance) + " meters", cam.description);
                Indicator.showIndicator(this,alert,alert);
                cam.setLastDistanceMessage(roundedDistance);
//                Log.i("rwqadar_pusht", location.getLatitude() + "_" + location.getLongitude() + " ," + cam.description + " ," + distance + " meters, " + roundedDistance);

            }
        }
    }


}
