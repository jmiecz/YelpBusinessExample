package net.mieczkowski.yelpbusinessexample.controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.bluelinelabs.conductor.RouterTransaction;

import net.mieczkowski.yelpbusinessexample.R;
import net.mieczkowski.yelpbusinessexample.controllers.base.BaseController;

import butterknife.OnClick;

/**
 * Created by Josh Mieczkowski on 10/21/2017.
 */

public class LocationController extends BaseController {

    private static final int REQUEST_CODE = 938543;

    @Override
    protected int getLayoutID() {
        return R.layout.controller_location;
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        requestForLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getRouter().setRoot(RouterTransaction.with(new SearchController()));
                }
                break;
        }
    }

    private void requestForLocation() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
    }

    private void openPermissionPage() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @OnClick(R.id.btnLocation)
    void onBtnLocationClicked() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestForLocation();
        } else {
            openPermissionPage();
        }
    }
}
