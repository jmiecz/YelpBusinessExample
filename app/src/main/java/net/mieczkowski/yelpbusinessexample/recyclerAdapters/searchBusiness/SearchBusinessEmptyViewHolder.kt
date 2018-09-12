package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness

import android.view.View

import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 10/22/2017.
 */

class SearchBusinessEmptyViewHolder(itemView: View) : BaseViewHolder<YelpBusiness>(itemView) {

    override fun onBind(item: YelpBusiness) {

    }
}
