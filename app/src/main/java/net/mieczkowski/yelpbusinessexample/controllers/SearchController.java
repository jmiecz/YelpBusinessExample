package net.mieczkowski.yelpbusinessexample.controllers;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mieczkowski.yelpbusinessexample.R;
import net.mieczkowski.yelpbusinessexample.controllers.base.BaseController;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class SearchController extends BaseController {

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {

    }
}
