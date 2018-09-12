package net.mieczkowski.dal.services.locationService

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
class LocationService(context: Context) {

    class MissingFineLocationError: Throwable("Missing Fine Location Permission")

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private var obsCount = 0
    private val locationSubject: PublishSubject<Location> = PublishSubject.create()
    private var locationRequest = LocationRequest().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private var locationCallBack: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            locationResult.locations.forEach {
                locationSubject.onNext(it)
            }
        }
    }

    fun configureLocationSettings(settings: LocationRequest.() -> Unit): LocationService{
        settings(locationRequest)

        return this
    }

    fun getLocation(): Single<Location> = getLocationObserver().take(1).toList().map { it[0] }

    @SuppressLint("MissingPermission")
    fun getLocationObserver(): Observable<Location> {
        return if (ContextCompat.checkSelfPermission(locationClient.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Observable.error(MissingFineLocationError())

        }else{
            locationSubject.subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        if(obsCount == 0)
                            locationClient.requestLocationUpdates(locationRequest, locationCallBack, null)

                        obsCount++
                    }
                    .doOnDispose {
                        obsCount--

                        if(obsCount == 0)
                            locationClient.removeLocationUpdates(locationCallBack)
                    }
        }
    }
}