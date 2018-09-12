package net.mieczkowski.yelpbusinessexample.interfaces.network;

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

    @GET(YelpInfo.Companion.getVERSION() + "businesses/search")
    Single<YelpBusinessWrapper> lookUpBusiness(@Query("term") String search,
                                               @Query("latitude") double latitude,
                                               @Query("longitude") double longitude);

    @GET(YelpInfo.Companion.getVERSION() + "businesses/{id}")
    Single<BusinessDetails> getBusinessDetails(@Path("id") String businessID);
}
