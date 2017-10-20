package net.mieczkowski.yelpbusinessexample.models.auth;

import android.os.Parcel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import net.mieczkowski.yelpbusinessexample.database.MyDatabase;
import net.mieczkowski.yelpbusinessexample.database.PrimaryKeyModel;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

@Table(database = MyDatabase.class)
public class YelpAuth extends PrimaryKeyModel {

    @Column
    @JsonProperty("access_token")
    String accessToken;

    @Column
    @JsonProperty("token_type")
    String tokenType;

    public YelpAuth() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.accessToken);
        dest.writeString(this.tokenType);
    }

    protected YelpAuth(Parcel in) {
        super(in);
        this.accessToken = in.readString();
        this.tokenType = in.readString();
    }

    public static final Creator<YelpAuth> CREATOR = new Creator<YelpAuth>() {
        @Override
        public YelpAuth createFromParcel(Parcel source) {
            return new YelpAuth(source);
        }

        @Override
        public YelpAuth[] newArray(int size) {
            return new YelpAuth[size];
        }
    };
}
