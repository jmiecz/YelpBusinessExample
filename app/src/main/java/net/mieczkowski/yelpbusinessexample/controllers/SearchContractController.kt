package net.mieczkowski.yelpbusinessexample.controllers

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.InputFilter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.controller_search.view.*
import net.mieczkowski.dal.cache.models.PreviousSearch
import net.mieczkowski.dal.exts.observeOnMain
import net.mieczkowski.dal.services.businessLookupService.BusinessLookupService
import net.mieczkowski.dal.services.businessLookupService.models.BusinessLookupRequest
import net.mieczkowski.dal.services.businessLookupService.models.MyLocation
import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness
import net.mieczkowski.dal.services.locationService.LocationService
import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.controllers.base.BaseController
import net.mieczkowski.yelpbusinessexample.exts.addDividerLine
import net.mieczkowski.yelpbusinessexample.exts.hideKeyboard
import net.mieczkowski.yelpbusinessexample.interfaces.PreviousSearchContract
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness.SearchBusinessAdapter
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory.SearchHistoryAdapter
import org.koin.standalone.inject

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

class SearchContractController(args: Bundle? = null) : BaseController(args), PreviousSearchContract {

    private val SEARCH_SAVE = "searchSave"
    private val CURRENT_QUERY = "currentQuery"
    private val allowChars = charArrayOf('!', '#', '$', '%', '&', '+', ',', '­', '.', '/', ':', '?', '@')

    private val locationService: LocationService by inject()
    private var locationDisposable: Disposable? = null
    private var myLocation: MyLocation? = null

    private val businessLookupService: BusinessLookupService by inject()
    private var businessDisposable: Disposable? = null

    private var searchHistoryAdapter: SearchHistoryAdapter? = null
    private var searchBusinessAdapter: SearchBusinessAdapter? = null

    private var currentSearch: String? = null
    private var currentQuery: String? = null

    private var filter: InputFilter = InputFilter { source, start, end, _, _, _ ->
        for (index in start until end) {
            val toCheck = source[index]
            if (allowChars.contains(toCheck))
                return@InputFilter null
            else if (!Character.isLetterOrDigit(toCheck) && !Character.isSpaceChar(toCheck))
                return@InputFilter ""
        }
        null
    }

    private lateinit var search: MenuItem
    private lateinit var searchView: SearchView
    private lateinit var editText: EditText

    init {
        setHasOptionsMenu(true)
    }

    override fun getLayoutID(): Int = R.layout.controller_search

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        setTitle(resources?.getString(R.string.business_lookup))

        listenForLocation()
        setRecyclerView(view)

        view.layoutRefresh.setOnRefreshListener {
            searchForBusinesses(editText.text.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        setupMenuSearch(menu)
        setupMenuEditText()
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        if (currentSearch?.isNotEmpty() == true) {
            outState.putString(SEARCH_SAVE, currentSearch)
        } else {
            val query = searchView.query.toString()
            if (query.isNotEmpty())
                outState.putString(CURRENT_QUERY, query)
        }
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        currentSearch = null
        currentQuery = null

        if (savedViewState.containsKey(SEARCH_SAVE)) {
            currentSearch = savedViewState.getString(SEARCH_SAVE)
        } else if (savedViewState.containsKey(CURRENT_QUERY)) {
            currentQuery = savedViewState.getString(CURRENT_QUERY)
        }
    }

    override fun onDestroyView(view: View) {
        businessDisposable?.dispose()
        locationDisposable?.dispose()

        super.onDestroyView(view)
    }

    override fun onPreviousSearchClicked(previousSearch: PreviousSearch) {
        searchView.setQuery(previousSearch.searchTerm, false)
        searchForBusinesses(previousSearch.searchTerm)
    }

    private fun listenForLocation() {
        locationDisposable?.dispose()
        locationDisposable = locationService.getLocationObserver().subscribe({
            setMyLocation(it)
        }, {
            it.printStackTrace()
        })
    }

    private fun setMyLocation(location: Location) {
        myLocation = MyLocation(location.latitude, location.longitude)
    }

    private fun showWelcome(){
        view?.layoutRefresh?.visibility = View.GONE
        view?.layoutWelcome?.visibility = View.VISIBLE
    }

    private fun hideWelcome(){
        view?.layoutWelcome?.visibility = View.GONE
    }

    private fun setupMenuSearch(menu: Menu) {
        search = menu.findItem(R.id.action_search)
        searchView = search.actionView as SearchView

        val searchClose = searchView.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_close_btn)
        searchClose?.setImageResource(R.drawable.ic_clear_white_24dp)

        search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                view?.layoutRefresh?.visibility = View.VISIBLE
                hideWelcome()
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                showWelcome()
                return true
            }
        })

        restoreSearchData()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val previousSearch = PreviousSearch().apply { searchTerm = query }
                previousSearch.save()

                searchHistoryAdapter?.addItem(previousSearch, 0)

                searchForBusinesses(query)

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    currentSearch = null
                    setSearchHistoryAdapter()
                }

                return true
            }
        })
    }

    private fun setupMenuEditText() {
        editText = searchView.findViewById(R.id.search_src_text)
        editText.setTextColor(ContextCompat.getColor(editText.context, R.color.title_bar_text_color))
        editText.setHintTextColor(ContextCompat.getColor(editText.context, R.color.hint_color))
        editText.setHint(R.string.search_for_business)
        editText.filters = arrayOf(filter)
    }

    private fun restoreSearchData() {
        currentSearch?.let {
            search.expandActionView()
            searchView.setQuery(it, false)
            searchView.clearFocus()
            getCacheBusinesses(it)

        } ?: currentQuery?.let {
            search.expandActionView()
            searchView.setQuery(it, false)

            setSearchHistoryAdapter()
        }
    }

    private fun getCacheBusinesses(currentSearch: String) {
        businessLookupService.cacheLookUpByName(currentSearch)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ yelpBusinesses ->
                    if (yelpBusinesses.isEmpty()) {
                        searchForBusinesses(currentSearch)
                    } else {
                        setSearchBusinessAdapter(yelpBusinesses)
                    }
                }, {
                    it.printStackTrace()
                })
    }

    private fun setRecyclerView(view: View) {
        view.recyclerView.let {
            it.layoutManager = LinearLayoutManager(applicationContext)
            it.addDividerLine {
                it.color(Color.LTGRAY)
                        .sizeResId(R.dimen.divider)
            }
        }
    }

    private fun setSearchHistoryAdapter() {
        view?.layoutRefresh?.isEnabled = false

        if (searchHistoryAdapter == null) {
            searchHistoryAdapter = SearchHistoryAdapter.newInstance(this)
        }

        view?.recyclerView?.let {
            val adapter = it.adapter
            if (adapter == null || adapter !is SearchHistoryAdapter) {
                it.adapter = searchHistoryAdapter
            }
        }
    }

    private fun setSearchBusinessAdapter(YelpBusinesss: List<YelpBusiness>) {
        view?.layoutRefresh?.let {
            it.isRefreshing = false
            it.isEnabled = true
        }

        searchBusinessAdapter = SearchBusinessAdapter(YelpBusinesss)
        view?.recyclerView?.adapter = searchBusinessAdapter
    }

    private fun searchForBusinesses(search: String) {
        currentSearch = search

        editText.hideKeyboard()
        view?.layoutRefresh?.isRefreshing = true

        myLocation?.let {
            businessDisposable?.dispose()
            businessDisposable = businessLookupService.lookUpByName(BusinessLookupRequest(search, it))
                    .observeOnMain()
                    .subscribe({
                        setSearchBusinessAdapter(it)
                    }, {
                        it.printStackTrace()
                        setSearchBusinessAdapter(ArrayList())
                    })

        } ?: let {
            Toast.makeText(applicationContext, "Grabbing your location, please wait.", Toast.LENGTH_LONG).show()
            locationService.getLocation()
                    .observeOnMain()
                    .subscribe({
                        setMyLocation(it)
                        searchForBusinesses(search)
                    }, {
                        it.printStackTrace()
                    })
        }

    }


}
