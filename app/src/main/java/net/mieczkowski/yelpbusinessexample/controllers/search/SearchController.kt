package net.mieczkowski.yelpbusinessexample.controllers.search

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import io.reactivex.Completable
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
import net.mieczkowski.yelpbusinessexample.interfaces.PreviousSearchContract
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness.SearchBusinessAdapter
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory.SearchHistoryAdapter
import org.koin.standalone.inject
import java.util.concurrent.TimeUnit

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

class SearchController(args: Bundle? = null) : BaseController(args), PreviousSearchContract {

    private val SEARCH_SAVE = "searchSave"
    private val CURRENT_QUERY = "currentQuery"

    private val locationService: LocationService by inject()
    private var locationDisposable: Disposable? = null
    private var myLocation: MyLocation? = null

    private val businessLookupService: BusinessLookupService by inject()
    private var businessDisposable: Disposable? = null

    private var searchHistoryAdapter: SearchHistoryAdapter? = null
    private var searchBusinessAdapter: SearchBusinessAdapter? = null

    private var currentSearch: String? = null
    private var currentQuery: String? = null

    private lateinit var searchViewHolder: SearchViewHolder

    override fun getLayoutID(): Int = R.layout.controller_search

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        setTitle(null)

        setupToolbar(view)
        listenForLocation()
        setRecyclerView(view)

        view.layoutRefresh.setOnRefreshListener {
            searchForBusinesses(searchViewHolder.getSearchQuery())
        }
    }

    private fun setupToolbar(view: View) {
        iToolbar.getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        }

        iToolbar.getToolBar().setNavigationOnClickListener {
            router.popController(this)
        }

        searchViewHolder = SearchViewHolder(view.context)
        iToolbar.getToolBar().addView(searchViewHolder.view)

        searchViewHolder.setOnQuerySelected { query ->
            val previousSearch = PreviousSearch().apply { searchTerm = query }
            previousSearch.save()

            searchHistoryAdapter?.addQuery(previousSearch)

            searchForBusinesses(query)
        }

        searchViewHolder.setOnTextChange { newText ->
            if (newText.isEmpty()) {
                currentSearch = null
                setSearchHistoryAdapter()
            }
        }

        restoreSearchData()
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        if (currentSearch?.isNotEmpty() == true) {
            outState.putString(SEARCH_SAVE, currentSearch)
        } else {
            val query = searchViewHolder.getSearchQuery()
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

    override fun onChangeStarted(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeStarted(changeHandler, changeType)

        if(changeType == ControllerChangeType.POP_EXIT || changeType == ControllerChangeType.PUSH_EXIT){
            iToolbar.getToolBar().removeView(searchViewHolder.view)
        }else{
            restoreSearchData()
        }
    }

    override fun onPreviousSearchClicked(previousSearch: PreviousSearch) {
        searchViewHolder.setCurrentSearchQuery(previousSearch.searchTerm)
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

    private fun restoreSearchData() {
        currentSearch?.let {
            searchViewHolder.setCurrentSearchQuery(it)
            getCacheBusinesses(it)

        } ?: currentQuery?.let {
            searchViewHolder.setCurrentSearchQuery(it)

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

        //TODO: Need to figure out what is causing this adapter to not load when controller starts
        Completable.timer(10, TimeUnit.MILLISECONDS)
                .observeOnMain()
                .subscribe { setSearchHistoryAdapter() }
    }

    private fun setSearchHistoryAdapter() {
        view?.layoutRefresh?.isEnabled = false

        if (searchHistoryAdapter == null) {
            searchHistoryAdapter = SearchHistoryAdapter.newInstance(this)
        }

        view?.recyclerView?.adapter = searchHistoryAdapter
    }

    private fun setSearchBusinessAdapter(YelpBusinesss: List<YelpBusiness>) {
        view?.layoutRefresh?.let {
            it.isRefreshing = false
            it.isEnabled = true
        }

        searchBusinessAdapter = SearchBusinessAdapter(YelpBusinesss, router)
        view?.recyclerView?.adapter = searchBusinessAdapter

        view?.requestFocus()
    }

    private fun searchForBusinesses(search: String) {
        currentSearch = search

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
