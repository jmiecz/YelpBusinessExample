package net.mieczkowski.yelpbusinessexample.interfaces


import net.mieczkowski.dal.cache.models.PreviousSearch

/**
 * Created by Josh Mieczkowski on 10/21/2017.
 */

interface PreviousSearchContract {

    fun onPreviousSearchClicked(previousSearch: PreviousSearch)
}
