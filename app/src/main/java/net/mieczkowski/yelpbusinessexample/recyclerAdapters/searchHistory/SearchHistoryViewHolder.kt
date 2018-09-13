package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.row_search_history.view.*
import net.mieczkowski.dal.cache.models.PreviousSearch
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 10/20/2017.
 */

class SearchHistoryViewHolder(itemView: View) : BaseViewHolder<PreviousSearch>(itemView) {

    val txtSearchHistory: TextView = itemView.txtSearchHistory

    override fun onBind(item: PreviousSearch?) {
        txtSearchHistory.text = item?.searchTerm
    }
}
