package net.mieczkowski.yelpbusinessexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_main.*
import net.mieczkowski.yelpbusinessexample.controllers.LocationController
import net.mieczkowski.yelpbusinessexample.controllers.WelcomeController
import net.mieczkowski.yelpbusinessexample.interfaces.IToolbar

class MainActivity : AppCompatActivity(), IToolbar {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val rootController: Controller = if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LocationController()
        } else {
            WelcomeController()
        }

        router = Conductor.attachRouter(this, controllerContainer, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(rootController))
        }

    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun getToolBar(): Toolbar = toolbar
}
