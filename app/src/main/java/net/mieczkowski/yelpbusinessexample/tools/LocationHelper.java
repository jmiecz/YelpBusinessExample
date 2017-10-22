package net.mieczkowski.yelpbusinessexample.tools;

import android.app.Activity;
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

import net.mieczkowski.yelpbusinessexample.interfaces.ILocation;

import java.util.Locale;

/**
 * Created by Josh Mieczkowski on 10/21/2017.
 */

public class LocationHelper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private ILocation iLocation;
    private GoogleApiClient googleApiClient;
    private Geocoder geocoder;

    //Per Google, don't use this until Google Play services version 12.0.0 is available, which is expected to ship in early 2018.
    //private FusedLocationProviderClient fusedLocationProviderClient;

    public LocationHelper(Activity activity, ILocation iLocation) {
        this.iLocation = iLocation;

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
            iLocation.onLocationReceived(location);
        }else{
            requestLocation();
        }

//        fusedLocationProviderClient.getLastLocation()
//                .addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if(task.isSuccessful() && task.getResult() != null){
//                            iLocation.onLocationReceived(location);
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
                iLocation.onLocationReceived(location);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
