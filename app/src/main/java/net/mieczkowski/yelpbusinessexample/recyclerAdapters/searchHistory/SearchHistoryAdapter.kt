package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory

import android.view.ViewGroup

import net.mieczkowski.dal.cache.models.PreviousSearch
import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.interfaces.PreviousSearchContract
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseAdapter
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 10/20/2017.
 */

class SearchHistoryAdapter(objects: List<PreviousSearch>, private val previousSearchContract: PreviousSearchContract) : BaseAdapter<PreviousSearch>(objects) {

    companion object {

        fun newInstance(previousSearchContract: PreviousSearchContract): SearchHistoryAdapter {
            return SearchHistoryAdapter(
                    PreviousSearch.getPreviousSearches(),
                    previousSearchContract
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<PreviousSearch> {
        return SearchHistoryViewHolder(getView(parent, R.layout.row_search_history))
    }

    override fun onRowClick(position: Int) {
        super.onRowClick(position)

        val previousSearch = objects[position]
        previousSearchContract.onPreviousSearchClicked(previousSearch)
    }


}
