package net.mieczkowski.yelpbusinessexample;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;

import net.mieczkowski.yelpbusinessexample.tools.ServiceChecker;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class MyApplication extends Application {

    static ServiceChecker serviceChecker;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);
        serviceChecker = new ServiceChecker(this);
        Stetho.initializeWithDefaults(this);
    }

    public static ServiceChecker getServiceChecker() {
        return serviceChecker;
    }
}
