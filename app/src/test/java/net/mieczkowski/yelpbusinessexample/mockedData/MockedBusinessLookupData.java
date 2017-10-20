package net.mieczkowski.yelpbusinessexample.mockedData;

import net.mieczkowski.yelpbusinessexample.models.business.BusinessDetails;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusinessWrapper;
import net.mieczkowski.yelpbusinessexample.networkInterfaces.IBusiness;
import net.mieczkowski.yelpbusinessexample.services.base.RetroService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class MockedBusinessLookupData implements IBusiness{

    @Override
    public Single<YelpBusinessWrapper> lookUpBusiness(@Query("name") String name, @Query("city") String city, @Query("state") String state, @Query("country") String country) {
        String json = "{\n" +
                "\t\"businesses\": [{\n" +
                "\t\t\"name\": \"Gary Danko\",\n" +
                "\t\t\"location\": {\n" +
                "\t\t\t\"address1\": \"800 N Point St\",\n" +
                "\t\t\t\"address2\": \"\",\n" +
                "\t\t\t\"address3\": \"\",\n" +
                "\t\t\t\"city\": \"San Francisco\",\n" +
                "\t\t\t\"state\": \"CA\",\n" +
                "\t\t\t\"postal_code\": \"94109\",\n" +
                "\t\t\t\"country\": \"US\"\n" +
                "\t\t},\n" +
                "\t\t\"coordinates\": {\n" +
                "\t\t\t\"latitude\": 37.80587,\n" +
                "\t\t\t\"longitude\": -122.42058\n" +
                "\t\t},\n" +
                "\t\t\"phone\": \"+14157492060\",\n" +
                "\t\t\"id\": \"gary-danko-san-francisco\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"name\": \"Good Grub Vending\",\n" +
                "\t\t\"location\": {\n" +
                "\t\t\t\"address1\": \"758 N Point St\",\n" +
                "\t\t\t\"address2\": \"\",\n" +
                "\t\t\t\"address3\": \"\",\n" +
                "\t\t\t\"city\": \"San Francisco\",\n" +
                "\t\t\t\"state\": \"CA\",\n" +
                "\t\t\t\"postal_code\": \"94109\",\n" +
                "\t\t\t\"country\": \"US\"\n" +
                "\t\t},\n" +
                "\t\t\"coordinates\": {\n" +
                "\t\t\t\"latitude\": 37.8061104,\n" +
                "\t\t\t\"longitude\": -122.4195633\n" +
                "\t\t},\n" +
                "\t\t\"phone\": \"+14157492060\",\n" +
                "\t\t\"id\": \"good-grub-vending-san-francisco\"\n" +
                "\t}]\n" +
                "}";

        try {
            YelpBusinessWrapper yelpBusinessWrapper = RetroService.getObjectMapper().readValue(json, YelpBusinessWrapper.class);

            return Single.just(yelpBusinessWrapper);
        } catch (IOException e) {
            return Single.error(e);
        }
    }

    @Override
    public Single<BusinessDetails> getBusinessDetails(@Path("id") String businessID) {
        String json = "{\n" +
                "\t\"id\": \"gary-danko-san-francisco\",\n" +
                "\t\"name\": \"Gary Danko\",\n" +
                "\t\"image_url\": \"https://s3-media4.fl.yelpcdn.com/bphoto/--8oiPVp0AsjoWHqaY1rDQ/o.jpg\",\n" +
                "\t\"is_claimed\": false,\n" +
                "\t\"is_closed\": false,\n" +
                "\t\"url\": \"https://www.yelp.com/biz/gary-danko-san-francisco\",\n" +
                "\t\"price\": \"$$$$\",\n" +
                "\t\"rating\": 4.5,\n" +
                "\t\"review_count\": 4521,\n" +
                "\t\"phone\": \"+14152520800\",\n" +
                "\t\"photos\": [\"http://s3-media3.fl.yelpcdn.com/bphoto/--8oiPVp0AsjoWHqaY1rDQ/o.jpg\",\n" +
                "\t\"http://s3-media2.fl.yelpcdn.com/bphoto/ybXbObsm7QGw3SGPA1_WXA/o.jpg\",\n" +
                "\t\"http://s3-media3.fl.yelpcdn.com/bphoto/7rZ061Wm4tRZ-iwAhkRSFA/o.jpg\"],\n" +
                "\t\"hours\": [{\n" +
                "\t\t\"hours_type\": \"REGULAR\",\n" +
                "\t\t\"open\": [{\n" +
                "\t\t\t\"is_overnight\": false,\n" +
                "\t\t\t\"end\": \"2200\",\n" +
                "\t\t\t\"day\": 0,\n" +
                "\t\t\t\"start\": \"1730\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"is_overnight\": false,\n" +
                "\t\t\t\"end\": \"2200\",\n" +
                "\t\t\t\"day\": 1,\n" +
                "\t\t\t\"start\": \"1730\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"is_overnight\": false,\n" +
                "\t\t\t\"end\": \"2200\",\n" +
                "\t\t\t\"day\": 2,\n" +
                "\t\t\t\"start\": \"1730\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"is_overnight\": false,\n" +
                "\t\t\t\"end\": \"2200\",\n" +
                "\t\t\t\"day\": 3,\n" +
                "\t\t\t\"start\": \"1730\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"is_overnight\": false,\n" +
                "\t\t\t\"end\": \"2200\",\n" +
                "\t\t\t\"day\": 4,\n" +
                "\t\t\t\"start\": \"1730\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"is_overnight\": false,\n" +
                "\t\t\t\"end\": \"2200\",\n" +
                "\t\t\t\"day\": 5,\n" +
                "\t\t\t\"start\": \"1730\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"is_overnight\": false,\n" +
                "\t\t\t\"end\": \"2200\",\n" +
                "\t\t\t\"day\": 6,\n" +
                "\t\t\t\"start\": \"1730\"\n" +
                "\t\t}],\n" +
                "\t\t\"is_open_now\": false\n" +
                "\t}],\n" +
                "\t\"categories\": [{\n" +
                "\t\t\"alias\": \"newamerican\",\n" +
                "\t\t\"title\": \"American (New)\"\n" +
                "\t}],\n" +
                "\t\"coordinates\": {\n" +
                "\t\t\"latitude\": 37.80587,\n" +
                "\t\t\"longitude\": -122.42058\n" +
                "\t},\n" +
                "\t\"location\": {\n" +
                "\t\t\"address1\": \"800 N Point St\",\n" +
                "\t\t\"address2\": \"\",\n" +
                "\t\t\"address3\": \"\",\n" +
                "\t\t\"city\": \"San Francisco\",\n" +
                "\t\t\"state\": \"CA\",\n" +
                "\t\t\"zip_code\": \"94109\",\n" +
                "\t\t\"country\": \"US\",\n" +
                "\t\t\"display_address\": [\"800 N Point St\",\n" +
                "\t\t\"San Francisco, CA 94109\"],\n" +
                "\t\t\"cross_streets\": \"Hyde St & Larkin St\"\n" +
                "\t},\n" +
                "\t\"transactions\": [\"restaurant_reservation\"]\n" +
                "}";

        try {
            BusinessDetails businessDetails = RetroService.getObjectMapper().readValue(json, BusinessDetails.class);
            return Single.just(businessDetails);
        } catch (IOException e) {
            return Single.error(e);
        }
    }
}
