package net.mieczkowski.yelpbusinessexample.controllers;

import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import net.mieczkowski.yelpbusinessexample.R;
import net.mieczkowski.yelpbusinessexample.controllers.base.BaseController;
import net.mieczkowski.yelpbusinessexample.interfaces.ILocationInfo;
import net.mieczkowski.yelpbusinessexample.interfaces.IPreviousSearch;
import net.mieczkowski.yelpbusinessexample.models.LocationInfo;
import net.mieczkowski.yelpbusinessexample.models.PreviousSearch;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessLookupRequest;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory.SearchHistoryAdapter;
import net.mieczkowski.yelpbusinessexample.services.YelpBusinessLookupService;
import net.mieczkowski.yelpbusinessexample.tools.LocationHelper;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class SearchController extends BaseController implements IPreviousSearch {

    @BindView(R.id.recyclerPreviousSearches)
    RecyclerView recyclerPreviousSearches;

    private LocationHelper locationHelper;
    private LocationInfo locationInfo;

    private char[] allowChars = new char[]{'!', '#', '$', '%', '&', '+', ',', '­', '.', '/', ':', '?', '@'};

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
        locationHelper = new LocationHelper(getActivity(), new ILocationInfo() {
            @Override
            public void onInfoReceived(LocationInfo locationInfo) {
                SearchController.this.locationInfo = locationInfo;
            }
        });

        locationHelper.onStart();

        setRecyclerPreviousSearches();
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        locationHelper.onStop();
        super.onDestroyView(view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        EditText editText = searchView.findViewById(R.id.search_src_text);
        editText.setFilters(new InputFilter[]{filter});

        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                recyclerPreviousSearches.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                recyclerPreviousSearches.setVisibility(View.GONE);

                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PreviousSearch previousSearch = new PreviousSearch(query);
                previousSearch.save();

                SearchHistoryAdapter searchHistoryAdapter = (SearchHistoryAdapter) recyclerPreviousSearches.getAdapter();
                searchHistoryAdapter.addItem(previousSearch, 0);

                recyclerPreviousSearches.setVisibility(View.GONE);

                searchForBusinesses(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    recyclerPreviousSearches.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });

    }

    private void setRecyclerPreviousSearches() {
        recyclerPreviousSearches.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerPreviousSearches.setAdapter(SearchHistoryAdapter.newInstance(this));

        recyclerPreviousSearches.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.GRAY)
                        .sizeResId(R.dimen.divider)
                        .build());
    }

    private void searchForBusinesses(String search){
        BusinessLookupRequest.Builder builder = BusinessLookupRequest.newBuilder().name(search);
        if(locationInfo != null){
            builder.city(locationInfo.getCity())
                    .state(locationInfo.getState());
        }

        new YelpBusinessLookupService().lookUpByName(builder.build())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<YelpBusiness>>() {
                    @Override
                    public void accept(ArrayList<YelpBusiness> yelpBusinesses) throws Exception {
                        //TODO:
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //TODO:
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onPreviousSearchClicked(PreviousSearch previousSearch) {
        searchForBusinesses(previousSearch.getSearchTerm());
    }
}
