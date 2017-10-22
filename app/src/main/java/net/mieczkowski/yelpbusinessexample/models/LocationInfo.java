package net.mieczkowski.yelpbusinessexample.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Josh Mieczkowski on 10/21/2017.
 */

public class LocationInfo implements Parcelable {

    private String city;
    private String state;

    public LocationInfo(String city, String state) {
        this.city = city;
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.state);
    }

    protected LocationInfo(Parcel in) {
        this.city = in.readString();
        this.state = in.readString();
    }

    public static final Parcelable.Creator<LocationInfo> CREATOR = new Parcelable.Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel source) {
            return new LocationInfo(source);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };
}
