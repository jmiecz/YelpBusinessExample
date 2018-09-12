package net.mieczkowski.yelpbusinessexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import net.mieczkowski.yelpbusinessexample.controllers.LocationController;
import net.mieczkowski.yelpbusinessexample.controllers.SearchController;
import net.mieczkowski.yelpbusinessexample.interfaces.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IToolbar {

    @BindView(R.id.controllerContainer)
    ViewGroup controllerContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        Controller rootController;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            rootController = new LocationController();
        } else {
            rootController = new SearchController();
        }

        router = Conductor.attachRouter(this, controllerContainer, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(rootController));
        }

        if (MainApplication.getServiceChecker().isConnected()) {
            new YelpAuthService().getYelpAuth().subscribe();
        }


    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }

}
