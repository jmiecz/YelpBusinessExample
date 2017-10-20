package net.mieczkowski.yelpbusinessexample.models.auth;

import net.mieczkowski.yelpbusinessexample.BuildConfig;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class YelpAuthRequest {

    private String grantType = "client_credentials";

    private String clientID = BuildConfig.YELP_CLIENT_ID;

    private String clientSecret = BuildConfig.YELP_CLIENT_SECRET;

    public YelpAuthRequest() {
    }

    public String getGrantType() {
        return grantType;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
