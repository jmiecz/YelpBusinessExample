package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.row_seach_business.view.*
import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness
import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.exts.loadUrl
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 10/22/2017.
 */

class SearchBusinessViewHolder(itemView: View) : BaseViewHolder<YelpBusiness>(itemView) {

    val imgIcon: ImageView = itemView.imgIcon

    val txtTitle: TextView = itemView.txtTitle
    val txtRatings: TextView = itemView.txtRatings
    val txtReviewCount: TextView = itemView.txtReviewCount

    init {
        ContextCompat.getDrawable(itemView.context, R.drawable.ic_star_rate_white_18dp)?.let {
            val ratingsDrawable = DrawableCompat.wrap(it)

            DrawableCompat.setTint(ratingsDrawable, Color.parseColor("#FFD700"))
            txtRatings.setCompoundDrawablesWithIntrinsicBounds(ratingsDrawable, null, null, null)
        }

        ContextCompat.getDrawable(itemView.context, R.drawable.ic_edit_black_18dp)?.let {
            val reviewCountDrawable = DrawableCompat.wrap(it)

            DrawableCompat.setTint(reviewCountDrawable, Color.parseColor("#A9A9A9"))
            txtReviewCount.setCompoundDrawablesWithIntrinsicBounds(reviewCountDrawable, null, null, null)
        }
    }

    override fun onBind(item: YelpBusiness?) {
        item?.let {
            imgIcon.loadUrl(it.businessDetails?.imgUrl)

            txtTitle.text = it.name

            txtRatings.text = it.businessDetails?.rating.toString()
            txtReviewCount.text = it.businessDetails?.reviewCount.toString()
        }

    }
}
