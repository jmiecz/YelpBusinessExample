package net.mieczkowski.yelpbusinessexample.controllers.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RestoreViewOnCreateController
import net.mieczkowski.yelpbusinessexample.interfaces.IToolbar
import org.koin.standalone.KoinComponent

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

abstract class BaseController(args: Bundle? = null) : RestoreViewOnCreateController(args), KoinComponent {

    protected val iToolbar: IToolbar
        get() {
            if (activity !is IToolbar) {
                throw RuntimeException("Need to implement IToolbar")
            }

            return activity as IToolbar
        }

    protected abstract fun getLayoutID(): Int

    protected abstract fun onViewBound(view: View, savedViewState: Bundle?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        val view = inflater.inflate(getLayoutID(), container, false)
        onViewBound(view, savedViewState)

        return view
    }

    fun setTitle(title: String?) {
        iToolbar.supportActionBar.title = title
    }

}
