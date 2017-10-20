package net.mieczkowski.yelpbusinessexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import net.mieczkowski.yelpbusinessexample.controllers.SearchController;
import net.mieczkowski.yelpbusinessexample.services.YelpAuthService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.controllerContainer)
    ViewGroup controllerContainer;

    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        router = Conductor.attachRouter(this, controllerContainer, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new SearchController()));
        }

        if(MyApplication.getServiceChecker().isConnected()){
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
