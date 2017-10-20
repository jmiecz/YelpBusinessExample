package net.mieczkowski.yelpbusinessexample.services;

import net.mieczkowski.yelpbusinessexample.mockedData.MockedAuthData;
import net.mieczkowski.yelpbusinessexample.models.auth.YelpAuth;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */
public class YelpAuthServiceTest {

    private YelpAuthService yelpAuthService;

    @Before
    public void setUp() throws Exception {
        yelpAuthService = new YelpAuthService(new MockedAuthData());
    }

    @Test
    public void getYelpAuth() throws Exception {
        yelpAuthService.getNewYelpAuth()
                .test()
                .assertNoErrors()
                .assertValue(new Predicate<YelpAuth>() {
                    @Override
                    public boolean test(@NonNull YelpAuth yelpAuth) throws Exception {
                        return yelpAuth.getAccessToken().equals("ACCESS_TOKEN")
                                && yelpAuth.getTokenType().equals("bearer");
                    }
                });
    }

}