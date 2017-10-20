package net.mieczkowski.yelpbusinessexample.networkInterfaces;

import net.mieczkowski.yelpbusinessexample.models.auth.YelpAuth;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public interface IAuth {

    @FormUrlEncoded
    @POST("oauth2/token")
    Single<YelpAuth> getYelpAuth(@Field("client_id") String clientID, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType);
}
