package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness

import android.view.ViewGroup
import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness

import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseAdapter
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 10/22/2017.
 */

class SearchBusinessAdapter(objects: List<YelpBusiness>) : BaseAdapter<YelpBusiness>(objects) {

    private val EMPTY_VIEW = 0
    private val BUSINESS_VIEW = 1

    override fun getItemViewType(position: Int): Int {
        return if (objects.isEmpty()) {
            EMPTY_VIEW
        } else {
            BUSINESS_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<YelpBusiness> {
        return when (viewType) {
            EMPTY_VIEW -> SearchBusinessEmptyViewHolder(getView(parent, R.layout.row_search_no_results))
            else -> SearchBusinessViewHolder(getView(parent, R.layout.row_seach_business))
        }
    }

    override fun getItemCount(): Int {
        return if (objects.isEmpty()) 1 else objects.size
    }


}
