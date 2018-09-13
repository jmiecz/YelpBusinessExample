package net.mieczkowski.yelpbusinessexample.controllers

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.view.View
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.controller_location.view.*
import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.controllers.base.BaseController
import net.mieczkowski.yelpbusinessexample.controllers.search.SearchController

/**
 * Created by Josh Mieczkowski on 10/21/2017.
 */

class LocationController : BaseController() {

    private val REQUEST_CODE = 938543

    override fun getLayoutID(): Int = R.layout.controller_location

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        setTitle(resources?.getString(R.string.location_permission))
        requestForLocation()

        view.btnLocation.setOnClickListener {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                requestForLocation()
            } else {
                openPermissionPage()
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                router.setRoot(RouterTransaction.with(SearchController()))
            }
        }
    }

    private fun requestForLocation() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE)
    }

    private fun openPermissionPage() {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", activity?.packageName, null)
        }

        startActivity(intent)
    }
}
