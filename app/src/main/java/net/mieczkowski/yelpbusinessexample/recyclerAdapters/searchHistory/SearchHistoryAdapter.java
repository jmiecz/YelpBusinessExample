package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory;

import android.view.ViewGroup;

import net.mieczkowski.yelpbusinessexample.R;
import net.mieczkowski.yelpbusinessexample.interfaces.IPreviousSearch;
import net.mieczkowski.yelpbusinessexample.models.PreviousSearch;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseAdapter;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Josh Mieczkowski on 10/20/2017.
 */

public class SearchHistoryAdapter extends BaseAdapter<PreviousSearch> {

    private IPreviousSearch iPreviousSearch;

    public static SearchHistoryAdapter newInstance(IPreviousSearch iPreviousSearch) {
        return new SearchHistoryAdapter(
                PreviousSearch.getPreviousSearches(),
                iPreviousSearch
        );
    }

    public SearchHistoryAdapter(List<PreviousSearch> objects, IPreviousSearch iPreviousSearch) {
        super(objects);

        this.iPreviousSearch = iPreviousSearch;
    }

    @Override
    public BaseViewHolder<PreviousSearch> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchHistoryViewHolder(getView(parent, R.layout.row_search_history));
    }

    @Override
    protected void onRowClick(int position) {
        super.onRowClick(position);

        PreviousSearch previousSearch = objects.get(position);
        iPreviousSearch.onPreviousSearchClicked(previousSearch);
    }
}
