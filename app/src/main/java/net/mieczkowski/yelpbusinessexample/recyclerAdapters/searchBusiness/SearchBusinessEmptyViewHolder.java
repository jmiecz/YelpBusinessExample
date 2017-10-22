package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness;

import android.view.View;
import android.view.ViewGroup;

import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseAdapter;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Josh Mieczkowski on 10/22/2017.
 */

public class SearchBusinessEmptyViewHolder extends BaseViewHolder<YelpBusiness> {

    public SearchBusinessEmptyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(YelpBusiness object) {

    }
}
