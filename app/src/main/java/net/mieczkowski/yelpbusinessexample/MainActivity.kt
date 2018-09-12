package net.mieczkowski.yelpbusinessexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

import net.mieczkowski.yelpbusinessexample.controllers.LocationController
import net.mieczkowski.yelpbusinessexample.controllers.SearchController
import net.mieczkowski.yelpbusinessexample.interfaces.IToolbar

import kotlinx.android.synthetic.main.activity_main.*
import net.mieczkowski.dal.services.authService.AuthService
import net.mieczkowski.dal.tools.ServiceChecker
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), IToolbar {

    private lateinit var router: Router

    private val serviceChecker: ServiceChecker by inject()
    private val authService: AuthService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val rootController: Controller = if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LocationController()
        } else {
            SearchController()
        }

        router = Conductor.attachRouter(this, controllerContainer, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(rootController))
        }

        if (serviceChecker.hasInternetAccess()) {
            authService.getYelpAuth().subscribe()
        }

    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

}
