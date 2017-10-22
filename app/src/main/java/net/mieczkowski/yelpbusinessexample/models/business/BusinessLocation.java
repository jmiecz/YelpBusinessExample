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
public class BusinessLocation extends PrimaryKeyModel {

    @Column
    @JsonProperty("address1")
    String address1;

    @Column
    @JsonProperty("address2")
    String address2;

    @Column
    @JsonProperty("address3")
    String address3;

    @Column
    @JsonProperty("city")
    String city;

    @Column
    @JsonProperty("state")
    String state;

    @Column
    @JsonProperty("postal_code")
    String postalCode;

    @Column
    @JsonProperty("country")
    String country;

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public BusinessLocation() {
    }

}
