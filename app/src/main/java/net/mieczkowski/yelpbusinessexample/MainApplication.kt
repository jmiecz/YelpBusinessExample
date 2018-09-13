package net.mieczkowski.yelpbusinessexample

import android.app.Application

import net.mieczkowski.dal.DAL
import org.koin.android.ext.android.startKoin


/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf())
        DAL.startInstance(this)
    }

}
