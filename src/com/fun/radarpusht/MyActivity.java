package com.fun.radarpusht;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {

	private RadarService service;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startBackground();
            }
        });


		List<Location> fakeLocations = new ArrayList<Location>();
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




    }

	private Location createLocation(Double lat, Double lon){
		Location location = new Location("data");
		location.setLongitude(lon);
		location.setLatitude(lat);
		return location;
	}


    public void startBackground() {
        Toast.makeText(this, "Starting background service..", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), RadarService.class);
        startService(intent);
    }
}
