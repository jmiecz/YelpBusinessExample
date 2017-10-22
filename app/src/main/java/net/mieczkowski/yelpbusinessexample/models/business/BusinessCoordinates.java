package net.mieczkowski.yelpbusinessexample.models.business;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import net.mieczkowski.yelpbusinessexample.database.MyDatabase;
import net.mieczkowski.yelpbusinessexample.database.PrimaryKeyModel;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

@Table(database = MyDatabase.class)
public class BusinessCoordinates extends PrimaryKeyModel {

    @Column
    @JsonProperty("latitude")
    double latitude;

    @Column
    @JsonProperty("longitude")
    double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public BusinessCoordinates() {
    }

}
