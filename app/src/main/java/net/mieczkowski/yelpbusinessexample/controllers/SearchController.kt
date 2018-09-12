package net.mieczkowski.yelpbusinessexample.controllers

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.InputFilter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.controller_search.view.*
import net.mieczkowski.dal.cache.models.PreviousSearch
import net.mieczkowski.dal.services.businessLookupService.BusinessLookupService
import net.mieczkowski.dal.services.businessLookupService.models.BusinessLookupRequest
import net.mieczkowski.dal.services.businessLookupService.models.MyLocation
import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness
import net.mieczkowski.dal.services.locationService.LocationService
import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.controllers.base.BaseController
import net.mieczkowski.yelpbusinessexample.exts.hideKeyboard
import net.mieczkowski.yelpbusinessexample.interfaces.IPreviousSearch
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness.SearchBusinessAdapter
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory.SearchHistoryAdapter
import org.koin.standalone.inject

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

class SearchController(args: Bundle? = null) : BaseController(args), IPreviousSearch {

    private val SEARCH_SAVE = "searchSave"
    private val CURRENT_QUERY = "currentQuery"

    lateinit var layoutRefresh: SwipeRefreshLayout

    lateinit var recyclerView: RecyclerView

    lateinit var layoutWelcome: View

    private var searchHistoryAdapter: SearchHistoryAdapter? = null
    private var searchBusinessAdapter: SearchBusinessAdapter? = null

    private val locationService: LocationService by inject()
    private lateinit var myLocation: MyLocation

    private val businessLookupService: BusinessLookupService by inject()

    private var currentSearch: String? = null
    private var currentQuery: String? = null
    private var activityResumed = false

    private val allowChars = charArrayOf('!', '#', '$', '%', '&', '+', ',', '­', '.', '/', ':', '?', '@')

    private var disposable: Disposable? = null
    private var locationDisposable: Disposable? = null

    private var filter: InputFilter = InputFilter { source, start, end, dest, dstart, dend ->
        for (index in start until end) {
            val toCheck = source[index]
            for (allowed in allowChars) {
                if (allowed == toCheck) {
                    return@InputFilter null
                }
            }

            if (!Character.isLetterOrDigit(toCheck) && !Character.isSpaceChar(toCheck)) {
                return@InputFilter ""
            }
        }
        null
    }

    private var search: MenuItem? = null
    private var searchView: SearchView? = null
    private var editText: EditText? = null

    init {
        setHasOptionsMenu(true)
    }

    override fun getLayoutID(): Int = R.layout.controller_search

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        layoutRefresh = view.layoutRefresh
        recyclerView = view.recyclerView
        layoutWelcome = view.layoutWelcome

        setTitle(resources?.getString(R.string.business_lookup))

        listenForLocation()
        setRecyclerView()

        layoutRefresh.setOnRefreshListener {
            editText?.text?.toString()?.let {
                searchForBusinesses(it)
            }
        }
    }

    private fun listenForLocation() {
        locationDisposable?.dispose()
        locationDisposable = locationService.getLocationObserver().subscribe({
            myLocation = MyLocation(it.latitude, it.longitude)
        }, {
            it.printStackTrace()
        })
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        if (currentSearch != null) {
            outState.putString(SEARCH_SAVE, currentSearch)
        } else if (searchView != null && !searchView!!.query.toString().isEmpty()) {
            outState.putString(CURRENT_QUERY, searchView!!.query.toString())
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
        disposable?.dispose()
        locationDisposable?.dispose()

        super.onDestroyView(view)
    }


    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        activityResumed = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        search = menu.findItem(R.id.action_search)
        searchView = search?.actionView as SearchView

        editText = searchView?.findViewById(R.id.search_src_text)
        editText?.let {
            it.setTextColor(ContextCompat.getColor(it.context, R.color.title_bar_text_color))
            it.setHintTextColor(ContextCompat.getColor(it.context, R.color.hint_color))
            it.setHint(R.string.search_for_business)
            it.filters = arrayOf(filter)
        }

        val searchClose = searchView?.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_close_btn)
        searchClose?.setImageResource(R.drawable.ic_clear_white_24dp)

        search?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                layoutRefresh.visibility = View.VISIBLE
                layoutWelcome.visibility = View.GONE
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                layoutRefresh.visibility = View.GONE
                layoutWelcome.visibility = View.VISIBLE
                return true
            }
        })

        restoreSearchData()

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val previousSearch = PreviousSearch().apply { searchTerm = query }
                //previousSearch.save()

                searchHistoryAdapter?.addItem(previousSearch, 0)

                searchForBusinesses(query)

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (activityResumed) {
                    activityResumed = false
                    return true
                }

                if (newText.isEmpty()) {
                    currentSearch = null
                    setSearchHistoryAdapter()
                }

                return true
            }
        })

    }

    private fun restoreSearchData() {
        currentSearch?.let {
            search?.expandActionView()
            searchView?.setQuery(it, false)
            searchView?.clearFocus()

            businessLookupService.cacheLookUpByName(it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ yelpBusinesses ->
                        if (yelpBusinesses.isEmpty()) {
                            searchForBusinesses(it)
                        } else {
                            setSearchBusinessAdapter(yelpBusinesses)
                        }
                    }, {

                    })

        } ?: currentQuery?.let {
            search?.expandActionView()
            searchView?.setQuery(it, false)

            setSearchHistoryAdapter()
        }
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        recyclerView.addDividerLine {
//            it.color(Color.LTGRAY)
//                    .sizeResId(R.dimen.divider)
//        }
    }

    private fun setSearchHistoryAdapter() {
        layoutRefresh.isEnabled = false

        if (searchHistoryAdapter == null) {
            searchHistoryAdapter = SearchHistoryAdapter.newInstance(this)
        }

        val adapter = recyclerView.adapter
        if (adapter == null || adapter !is SearchHistoryAdapter) {
            recyclerView.adapter = searchHistoryAdapter
        }
    }

    private fun setSearchBusinessAdapter(yelpBusinesses: List<YelpBusiness>) {
        layoutRefresh.isRefreshing = false
        layoutRefresh.isEnabled = true

        searchBusinessAdapter = SearchBusinessAdapter(yelpBusinesses)
        recyclerView.adapter = searchBusinessAdapter
    }

    private fun searchForBusinesses(search: String) {
        currentSearch = search

        editText?.hideKeyboard()
        layoutRefresh.isRefreshing = true

        disposable?.dispose()
        disposable = businessLookupService.lookUpByName(BusinessLookupRequest(search, myLocation))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setSearchBusinessAdapter(it)
                }, {
                    it.printStackTrace()
                    setSearchBusinessAdapter(ArrayList())
                })
    }

    override fun onPreviousSearchClicked(previousSearch: PreviousSearch) {
        searchView?.setQuery(previousSearch.searchTerm, false)
        searchForBusinesses(previousSearch.searchTerm)
    }


}
