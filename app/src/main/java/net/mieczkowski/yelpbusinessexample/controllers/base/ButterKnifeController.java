package net.mieczkowski.yelpbusinessexample.controllers.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class ButterKnifeController extends Controller {

    private Unbinder unbinder;

    protected ButterKnifeController() {
    }

    protected ButterKnifeController(Bundle args) {
        super(args);
    }

    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(getLayoutID(), container, false);
    }

    @LayoutRes
    protected abstract int getLayoutID();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflateView(inflater, container);
        unbinder = ButterKnife.bind(this, view);
        onViewBound(view);
        return view;
    }

    protected abstract void onViewBound(@NonNull View view);

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }

}