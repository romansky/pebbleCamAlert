package com.fun.radarpusht;

import android.location.Location;

/**
 * Created by IntelliJ IDEA.
 * User: romansky
 * Date: 2/16/12
 * Time: 1:28 AM
 */
public class CameraData {
    public final String name;
    public final String description;
    public final String coordinateLon;
    public final String coordinateLat;
    private Location location;
    private int lastDistanceMessage = 6;

    public CameraData(String name, String description, String coordinateLon, String coordinateLat) {
        this.name = name;
        this.description = description;
        this.coordinateLon = coordinateLon;
        this.coordinateLat = coordinateLat;
    }

    public Location getLocation() {
        if (location == null) {
            location = new Location("data");
            location.setLongitude(Double.parseDouble(coordinateLon));
            location.setLatitude(Double.parseDouble(coordinateLat));
        }
        return location;
    }

    public int getLastDistanceMessage() {
        return lastDistanceMessage;
    }

    public void setLastDistanceMessage(int lastDistanceMessage) {
        this.lastDistanceMessage = lastDistanceMessage;
    }
}
