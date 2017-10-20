package net.mieczkowski.yelpbusinessexample.networkInterfaces;

import net.mieczkowski.yelpbusinessexample.models.business.BusinessDetails;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusinessWrapper;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public interface IBusiness {

    @GET(IYelp.VERSION + "businesses/matches/lookup")
    Single<YelpBusinessWrapper> lookUpBusiness(@Query("name") String name,
                                               @Query("city") String city,
                                               @Query("state") String state,
                                               @Query("country") String country);

    @GET(IYelp.VERSION + "businesses/{id}")
    Single<BusinessDetails> getBusinessDetails(@Path("id") String businessID);
}
