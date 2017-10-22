package net.mieczkowski.yelpbusinessexample.mockedData;

import net.mieczkowski.yelpbusinessexample.models.auth.YelpAuth;
import net.mieczkowski.yelpbusinessexample.interfaces.network.IAuth;
import net.mieczkowski.yelpbusinessexample.services.base.RetroService;

import java.io.IOException;

import io.reactivex.Single;
import retrofit2.http.Field;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class MockedAuthData implements IAuth {
    @Override
    public Single<YelpAuth> getYelpAuth(@Field("client_id") String clientID, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType) {
        String json = "{\n" +
                "  \"access_token\": \"ACCESS_TOKEN\",\n" +
                "  \"token_type\": \"bearer\",\n" +
                "  \"expires_in\": 15552000\n" +
                "}";

        try {
            YelpAuth yelpAuth = RetroService.getObjectMapper().readValue(json, YelpAuth.class);
            return Single.just(yelpAuth);
        } catch (IOException e) {
            return Single.error(e);
        }
    }
}
