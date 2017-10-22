package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness;

import android.view.ViewGroup;

import net.mieczkowski.yelpbusinessexample.R;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseAdapter;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Josh Mieczkowski on 10/22/2017.
 */

public class SearchBusinessAdapter extends BaseAdapter<YelpBusiness> {

    private static final int EMPTY_VIEW = 0;
    private static final int BUSINESS_VIEW = 1;

    public SearchBusinessAdapter(List<YelpBusiness> objects) {
        super(objects);
    }

    @Override
    public int getItemViewType(int position) {
        if(objects.isEmpty()){
            return EMPTY_VIEW;
        }else{
            return BUSINESS_VIEW;
        }
    }

    @Override
    public BaseViewHolder<YelpBusiness> onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case EMPTY_VIEW:
                return new SearchBusinessEmptyViewHolder(getView(parent, R.layout.row_search_no_results));
            default:
                return new SearchBusinessViewHolder(getView(parent, R.layout.row_seach_business));
        }
    }

    @Override
    public int getItemCount() {
        return objects.isEmpty() ? 1 : objects.size();
    }
}
