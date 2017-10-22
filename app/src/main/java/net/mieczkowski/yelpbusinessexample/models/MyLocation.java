package net.mieczkowski.yelpbusinessexample.models;

/**
 * Created by Josh Mieczkowski on 10/21/2017.
 */

public class MyLocation {

    private double latitude;
    private double longitude;

    public MyLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
