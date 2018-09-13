package net.mieczkowski.yelpbusinessexample.interfaces

import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

interface IToolbar {

    fun getSupportActionBar(): ActionBar?
    fun getToolBar(): Toolbar
}
