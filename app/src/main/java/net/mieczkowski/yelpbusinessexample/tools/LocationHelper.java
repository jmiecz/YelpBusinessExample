package net.mieczkowski.yelpbusinessexample.tools;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import net.mieczkowski.yelpbusinessexample.interfaces.ILocationInfo;
import net.mieczkowski.yelpbusinessexample.models.LocationInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Josh Mieczkowski on 10/21/2017.
 */

public class LocationHelper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private ILocationInfo iLocationInfo;
    private GoogleApiClient googleApiClient;
    private Geocoder geocoder;

    //Per Google, don't use this until Google Play services version 12.0.0 is available, which is expected to ship in early 2018.
    //private FusedLocationProviderClient fusedLocationProviderClient;

    public static final Map<String, String> STATES;
    static {
        STATES = new HashMap<String, String>();
        STATES.put("Alabama","AL");
        STATES.put("Alaska","AK");
        STATES.put("Alberta","AB");
        STATES.put("American Samoa","AS");
        STATES.put("Arizona","AZ");
        STATES.put("Arkansas","AR");
        STATES.put("Armed Forces (AE)","AE");
        STATES.put("Armed Forces Americas","AA");
        STATES.put("Armed Forces Pacific","AP");
        STATES.put("British Columbia","BC");
        STATES.put("California","CA");
        STATES.put("Colorado","CO");
        STATES.put("Connecticut","CT");
        STATES.put("Delaware","DE");
        STATES.put("District Of Columbia","DC");
        STATES.put("Florida","FL");
        STATES.put("Georgia","GA");
        STATES.put("Guam","GU");
        STATES.put("Hawaii","HI");
        STATES.put("Idaho","ID");
        STATES.put("Illinois","IL");
        STATES.put("Indiana","IN");
        STATES.put("Iowa","IA");
        STATES.put("Kansas","KS");
        STATES.put("Kentucky","KY");
        STATES.put("Louisiana","LA");
        STATES.put("Maine","ME");
        STATES.put("Manitoba","MB");
        STATES.put("Maryland","MD");
        STATES.put("Massachusetts","MA");
        STATES.put("Michigan","MI");
        STATES.put("Minnesota","MN");
        STATES.put("Mississippi","MS");
        STATES.put("Missouri","MO");
        STATES.put("Montana","MT");
        STATES.put("Nebraska","NE");
        STATES.put("Nevada","NV");
        STATES.put("New Brunswick","NB");
        STATES.put("New Hampshire","NH");
        STATES.put("New Jersey","NJ");
        STATES.put("New Mexico","NM");
        STATES.put("New York","NY");
        STATES.put("Newfoundland","NF");
        STATES.put("North Carolina","NC");
        STATES.put("North Dakota","ND");
        STATES.put("Northwest Territories","NT");
        STATES.put("Nova Scotia","NS");
        STATES.put("Nunavut","NU");
        STATES.put("Ohio","OH");
        STATES.put("Oklahoma","OK");
        STATES.put("Ontario","ON");
        STATES.put("Oregon","OR");
        STATES.put("Pennsylvania","PA");
        STATES.put("Prince Edward Island","PE");
        STATES.put("Puerto Rico","PR");
        STATES.put("Quebec","QC");
        STATES.put("Rhode Island","RI");
        STATES.put("Saskatchewan","SK");
        STATES.put("South Carolina","SC");
        STATES.put("South Dakota","SD");
        STATES.put("Tennessee","TN");
        STATES.put("Texas","TX");
        STATES.put("Utah","UT");
        STATES.put("Vermont","VT");
        STATES.put("Virgin Islands","VI");
        STATES.put("Virginia","VA");
        STATES.put("Washington","WA");
        STATES.put("West Virginia","WV");
        STATES.put("Wisconsin","WI");
        STATES.put("Wyoming","WY");
        STATES.put("Yukon Territory","YT");
    }


    public LocationHelper(Activity activity, ILocationInfo iLocationInfo) {
        this.iLocationInfo = iLocationInfo;

        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        geocoder = new Geocoder(activity, Locale.getDefault());

        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void onStart() {
        googleApiClient.connect();
    }

    public void onStop() {
        googleApiClient.disconnect();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location != null){
            getAddressFromLocation(location);
        }else{
            requestLocation();
        }

//        fusedLocationProviderClient.getLastLocation()
//                .addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if(task.isSuccessful() && task.getResult() != null){
//                            iLocation.onInfoReceived(task.getResult());
//                        }
//                    }
//                });
    }

    @SuppressWarnings("MissingPermission")
    private void requestLocation(){
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getAddressFromLocation(location);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getAddressFromLocation(Location location){
        Single.just(location)
                .map(new Function<Location, LocationInfo>() {
                    @Override
                    public LocationInfo apply(@io.reactivex.annotations.NonNull Location location) throws Exception {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Address address = addresses.get(0);

                        String city = address.getLocality();
                        String state = STATES.get(address.getAdminArea());

                        return new LocationInfo(city, state);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LocationInfo>() {
                    @Override
                    public void accept(LocationInfo locationInfo) throws Exception {
                        iLocationInfo.onInfoReceived(locationInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        requestLocation();
                    }
                });
    }

}
