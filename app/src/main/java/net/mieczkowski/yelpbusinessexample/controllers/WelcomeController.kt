package net.mieczkowski.yelpbusinessexample.controllers

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.controllers.base.BaseController
import net.mieczkowski.yelpbusinessexample.controllers.search.SearchController
import net.mieczkowski.yelpbusinessexample.exts.show

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
class WelcomeController(args: Bundle? = null) : BaseController(args) {

    override fun getLayoutID(): Int = R.layout.controller_welcome

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        setTitle(resources?.getString(R.string.business_lookup))

        iToolbar.getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.action_search)
        search.setOnMenuItemClickListener {
            SearchController().show(this)
            true
        }
    }

    override fun onChangeStarted(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeStarted(changeHandler, changeType)

        if(changeType == ControllerChangeType.POP_EXIT || changeType == ControllerChangeType.PUSH_EXIT){
            setHasOptionsMenu(false)
        }else{
            setHasOptionsMenu(true)
        }
    }
}