package net.mieczkowski.yelpbusinessexample.models.business;

import android.location.Location;

import net.mieczkowski.yelpbusinessexample.models.MyLocation;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class BusinessLookupRequest {

    private String searchTerm;
    private MyLocation location = new MyLocation(0, 0);

    public BusinessLookupRequest(String searchTerm, MyLocation location) {
        this.searchTerm = searchTerm;

        if(location != null) {
            this.location = location;
        }
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getSearchKey(){
        return searchTerm + getLocation().getLatitude() + getLocation().getLongitude();
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public MyLocation getLocation() {
        return location;
    }

    public void setLocation(MyLocation location) {
        this.location = location;
    }
}
