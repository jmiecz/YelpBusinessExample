package net.mieczkowski.yelpbusinessexample.models.business;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class BusinessLookupRequest {

    private String name;
    private String city;
    private String state;
    private String country = "US";

    private BusinessLookupRequest(Builder builder) {
        name = builder.name;
        city = builder.city;
        state = builder.state;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getSearchKey() {
        return name + city + state + country;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String name;
        private String city;
        private String state;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder city(String val) {
            city = val;
            return this;
        }

        public Builder state(String val) {
            state = val;
            return this;
        }

        public BusinessLookupRequest build() {
            return new BusinessLookupRequest(this);
        }
    }
}
