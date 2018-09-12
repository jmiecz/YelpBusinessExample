package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory

import android.view.ViewGroup

import net.mieczkowski.dal.cache.models.PreviousSearch
import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.interfaces.IPreviousSearch
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseAdapter
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 10/20/2017.
 */

class SearchHistoryAdapter(objects: List<PreviousSearch>, private val iPreviousSearch: IPreviousSearch) : BaseAdapter<PreviousSearch>(objects) {

    companion object {

        fun newInstance(iPreviousSearch: IPreviousSearch): SearchHistoryAdapter {
            return SearchHistoryAdapter(
                    PreviousSearch.getPreviousSearches(),
                    iPreviousSearch
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<PreviousSearch> {
        return SearchHistoryViewHolder(getView(parent, R.layout.row_search_history))
    }

    override fun onRowClick(position: Int) {
        super.onRowClick(position)

        val previousSearch = objects[position]
        iPreviousSearch.onPreviousSearchClicked(previousSearch)
    }


}
