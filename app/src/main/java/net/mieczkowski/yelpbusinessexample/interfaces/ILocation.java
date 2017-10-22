package net.mieczkowski.yelpbusinessexample.interfaces;

import android.location.Location;

/**
 * Created by Josh Mieczkowski on 10/21/2017.
 */

public interface ILocation {

    void onLocationReceived(Location location);
}
