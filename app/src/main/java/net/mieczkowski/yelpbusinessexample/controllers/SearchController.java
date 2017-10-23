package net.mieczkowski.yelpbusinessexample.controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import net.mieczkowski.yelpbusinessexample.R;
import net.mieczkowski.yelpbusinessexample.controllers.base.BaseController;
import net.mieczkowski.yelpbusinessexample.interfaces.ILocation;
import net.mieczkowski.yelpbusinessexample.interfaces.IPreviousSearch;
import net.mieczkowski.yelpbusinessexample.models.MyLocation;
import net.mieczkowski.yelpbusinessexample.models.PreviousSearch;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessLookupRequest;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness.SearchBusinessAdapter;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory.SearchHistoryAdapter;
import net.mieczkowski.yelpbusinessexample.services.YelpBusinessLookupService;
import net.mieczkowski.yelpbusinessexample.tools.LocationHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class SearchController extends BaseController implements IPreviousSearch {

    private static final String SEARCH_SAVE = "searchSave";
    private static final String CURRENT_QUERY = "currentQuery";

    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout layoutRefresh;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.layoutWelcome)
    View layoutWelcome;

    private SearchHistoryAdapter searchHistoryAdapter;
    private SearchBusinessAdapter searchBusinessAdapter;

    private LocationHelper locationHelper;
    private MyLocation myLocation;

    private String currentSearch;
    private String currentQuery;
    private boolean activityResumed = false;

    private char[] allowChars = new char[]{'!', '#', '$', '%', '&', '+', ',', '­', '.', '/', ':', '?', '@'};

    private Disposable disposable;

    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            for (int index = start; index < end; index++) {
                char toCheck = source.charAt(index);
                for (char allowed : allowChars) {
                    if (allowed == toCheck) {
                        return null;
                    }
                }

                if (!Character.isLetterOrDigit(toCheck) && !Character.isSpaceChar(toCheck)) {
                    return "";
                }
            }
            return null;
        }
    };

    public SearchController() {
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.controller_search;
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        setTitle(getResources().getString(R.string.business_lookup));

        locationHelper = new LocationHelper(getActivity(), new ILocation() {
            @Override
            public void onLocationReceived(Location location) {
                myLocation = new MyLocation(location.getLatitude(), location.getLongitude());
            }
        });

        locationHelper.onStart();

        setRecyclerView();

        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchForBusinesses(editText.getText().toString());
            }
        });
    }

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        super.onSaveViewState(view, outState);
        if(currentSearch != null){
            outState.putString(SEARCH_SAVE, currentSearch);
        }else if(searchView != null && !searchView.getQuery().toString().isEmpty()){
            outState.putString(CURRENT_QUERY, searchView.getQuery().toString());
        }
    }

    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        super.onRestoreViewState(view, savedViewState);
        currentSearch = null;
        currentQuery = null;

        if(savedViewState.containsKey(SEARCH_SAVE)){
            currentSearch = savedViewState.getString(SEARCH_SAVE);
        }else if(savedViewState.containsKey(CURRENT_QUERY)){
            currentQuery = savedViewState.getString(CURRENT_QUERY);
        }
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        locationHelper.onStop();
        if(disposable != null){
            disposable.dispose();
        }

        super.onDestroyView(view);
    }

    @Override
    protected void onActivityResumed(@NonNull Activity activity) {
        super.onActivityResumed(activity);

    }

    @Override
    protected void onActivityPaused(@NonNull Activity activity) {
        super.onActivityPaused(activity);
        activityResumed = true;
    }

    private MenuItem search;
    private SearchView searchView;
    private EditText editText;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        search = menu.findItem(R.id.action_search);
        searchView = (SearchView) search.getActionView();

        editText = searchView.findViewById(R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(getActivity(), R.color.title_bar_text_color));
        editText.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.hint_color));
        editText.setHint(R.string.search_for_business);
        editText.setFilters(new InputFilter[]{filter});

        ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear_white_24dp);

        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                layoutRefresh.setVisibility(View.VISIBLE);
                layoutWelcome.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                layoutRefresh.setVisibility(View.GONE);
                layoutWelcome.setVisibility(View.VISIBLE);
                return true;
            }
        });

        restoreSearchData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PreviousSearch previousSearch = new PreviousSearch(query);
                previousSearch.save();

                searchHistoryAdapter.addItem(previousSearch, 0);

                searchForBusinesses(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(activityResumed){
                    activityResumed = false;
                    return true;
                }

                if (newText.isEmpty()) {
                    currentSearch = null;
                    setSearchHistoryAdapter();
                }

                return true;
            }
        });

    }

    private void restoreSearchData(){
        if(currentSearch != null){
            search.expandActionView();
            searchView.setQuery(currentSearch, false);
            searchView.clearFocus();

            new YelpBusinessLookupService().lookUpByNameCache(currentSearch)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ArrayList<YelpBusiness>>() {
                        @Override
                        public void accept(ArrayList<YelpBusiness> yelpBusinesses) throws Exception {
                            if(yelpBusinesses.isEmpty()){
                                searchForBusinesses(currentSearch);
                            }else{
                                setSearchBusinessAdapter(yelpBusinesses);
                            }
                        }
                    });

        }else if(currentQuery != null){
            search.expandActionView();
            searchView.setQuery(currentQuery, false);

            setSearchHistoryAdapter();
        }
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.LTGRAY)
                        .sizeResId(R.dimen.divider)
                        .build());
    }

    private void setSearchHistoryAdapter(){
        layoutRefresh.setEnabled(false);

        if(searchHistoryAdapter == null) {
            searchHistoryAdapter = SearchHistoryAdapter.newInstance(this);
        }

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if(adapter == null || !(adapter instanceof SearchHistoryAdapter)){
            recyclerView.setAdapter(searchHistoryAdapter);
        }
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setSearchBusinessAdapter(List<YelpBusiness> yelpBusinesses){
        layoutRefresh.setRefreshing(false);
        layoutRefresh.setEnabled(true);

        searchBusinessAdapter = new SearchBusinessAdapter(yelpBusinesses);
        recyclerView.setAdapter(searchBusinessAdapter);
    }

    private void searchForBusinesses(String search){
        currentSearch = search;

        hideKeyboard();
        layoutRefresh.setRefreshing(true);

        if(disposable != null){
            disposable.dispose();
        }

        disposable = new YelpBusinessLookupService().lookUpByName(new BusinessLookupRequest(search, myLocation))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<YelpBusiness>>() {
                    @Override
                    public void accept(ArrayList<YelpBusiness> yelpBusinesses) throws Exception {
                        setSearchBusinessAdapter(yelpBusinesses);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        setSearchBusinessAdapter(new ArrayList<YelpBusiness>());
                    }
                });
    }

    @Override
    public void onPreviousSearchClicked(PreviousSearch previousSearch) {
        searchView.setQuery(previousSearch.getSearchTerm(), false);
        searchForBusinesses(previousSearch.getSearchTerm());
    }
}
