package net.mieczkowski.yelpbusinessexample.exts

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
fun View.hideKeyboard(){
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}