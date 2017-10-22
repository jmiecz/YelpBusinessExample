package net.mieczkowski.yelpbusinessexample.interfaces;

import android.location.Location;

import net.mieczkowski.yelpbusinessexample.models.LocationInfo;

/**
 * Created by Josh Mieczkowski on 10/21/2017.
 */

public interface ILocationInfo {

    void onInfoReceived(LocationInfo locationInfo);
}
