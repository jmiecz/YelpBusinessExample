package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness

import android.view.ViewGroup
import com.bluelinelabs.conductor.Router
import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness

import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.controllers.DetailController
import net.mieczkowski.yelpbusinessexample.exts.show
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseAdapter
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 10/22/2017.
 */

class SearchBusinessAdapter(objects: List<YelpBusiness>, private val router: Router) : BaseAdapter<YelpBusiness>(objects) {

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

    override fun onRowClick(position: Int) {
        super.onRowClick(position)

        DetailController(objects[position]).show(router)
    }
}
