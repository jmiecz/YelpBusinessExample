package net.mieczkowski.yelpbusinessexample.services;

import android.location.Location;

import net.mieczkowski.yelpbusinessexample.mockedData.MockedBusinessLookupData;
import net.mieczkowski.yelpbusinessexample.models.MyLocation;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessCoordinates;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessDetails;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessLocation;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessLookupRequest;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */
public class YelpBusinessLookupServiceTest {

    private YelpBusinessLookupService yelpBusinessLookupService;

    @Before
    public void setUp() throws Exception {
        yelpBusinessLookupService = new YelpBusinessLookupService(new MockedBusinessLookupData());
    }

    @Test
    public void newLookUpByName() throws Exception {
        BusinessLookupRequest businessLookupRequest = new BusinessLookupRequest("TEST", new MyLocation(0, 0));

        yelpBusinessLookupService.newLookUpByName(businessLookupRequest)
                .test()
                .assertNoErrors()
                .assertValue(new Predicate<ArrayList<YelpBusiness>>() {
                    @Override
                    public boolean test(@NonNull ArrayList<YelpBusiness> yelpBusinesses) throws Exception {
                        if(yelpBusinesses.size() != 2){
                            return false;
                        }

                        YelpBusiness yelpBusiness = yelpBusinesses.get(0);
                        BusinessLocation businessLocation = yelpBusiness.getBusinessLocation();
                        BusinessCoordinates businessCoordinates = yelpBusiness.getBusinessCoordinates();
                        BusinessDetails businessDetails = yelpBusiness.getBusinessDetails();

                        ArrayList<String> photos = new ArrayList<>();
                        photos.add("http://s3-media3.fl.yelpcdn.com/bphoto/--8oiPVp0AsjoWHqaY1rDQ/o.jpg");
                        photos.add("http://s3-media2.fl.yelpcdn.com/bphoto/ybXbObsm7QGw3SGPA1_WXA/o.jpg");
                        photos.add("http://s3-media3.fl.yelpcdn.com/bphoto/7rZ061Wm4tRZ-iwAhkRSFA/o.jpg");

                        return yelpBusiness.getName().equals("Gary Danko")
                                && yelpBusiness.getPhone().equals("+14157492060")
                                && yelpBusiness.getId().equals("gary-danko-san-francisco")
                                && yelpBusiness.getSearchKey().equals("TEST")
                                && businessLocation.getAddress1().equals("800 N Point St")
                                && businessLocation.getAddress2().isEmpty()
                                && businessLocation.getAddress3().isEmpty()
                                && businessLocation.getCity().equals("San Francisco")
                                && businessLocation.getState().equals("CA")
                                && businessLocation.getPostalCode().equals("94109")
                                && businessLocation.getCountry().equals("US")
                                && businessCoordinates.getLatitude() == 37.80587
                                && businessCoordinates.getLongitude() == -122.42058
                                && businessDetails.getId().equals(yelpBusiness.getId())
                                && businessDetails.getImgUrl().equals("https://s3-media4.fl.yelpcdn.com/bphoto/--8oiPVp0AsjoWHqaY1rDQ/o.jpg")
                                && !businessDetails.isClaimed()
                                && !businessDetails.isClosed()
                                && businessDetails.getBusinessUrl().equals("https://www.yelp.com/biz/gary-danko-san-francisco")
                                && businessDetails.getPriceRating().equals("$$$$")
                                && businessDetails.getRating() == 4.5
                                && businessDetails.getReviewCount() == 4521
                                && businessDetails.getBusinessPhotos().equals(photos);
                    }
                });
    }

}