package net.mieczkowski.yelpbusinessexample.models.business;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class YelpBusinessWrapper {

    @JsonProperty("businesses")
    private ArrayList<YelpBusiness> yelpBusinesses;

    public YelpBusinessWrapper() {
    }

    public ArrayList<YelpBusiness> getYelpBusinesses() {

        return yelpBusinesses;
    }

    public void setYelpBusinesses(ArrayList<YelpBusiness> yelpBusinesses) {
        this.yelpBusinesses = yelpBusinesses;
    }
}
