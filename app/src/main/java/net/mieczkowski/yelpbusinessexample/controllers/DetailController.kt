package net.mieczkowski.yelpbusinessexample.controllers

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.controller_details.view.*

import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness
import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.controllers.base.BaseController
import net.mieczkowski.yelpbusinessexample.exts.loadUrl

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
class DetailController(args: Bundle? = null) : BaseController(args) {

    companion object {
        private const val BUSINESS_TAG = "businessTag"
    }

    constructor(yelpBusiness: YelpBusiness): this(Bundle().apply {
        putString(BUSINESS_TAG, yelpBusiness.id)
    })

    lateinit var yelpBusiness: YelpBusiness

    override fun getLayoutID(): Int = R.layout.controller_details

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        YelpBusiness.getYelpBusiness(args.getString(BUSINESS_TAG))?.let { setupView(view, it)  }


    }

    private fun setupView(view: View, yelpBusiness: YelpBusiness){
        this.yelpBusiness = yelpBusiness

        view.imgIcon.loadUrl(yelpBusiness.businessDetails?.imgUrl)
        view.txtName.text = yelpBusiness.name
        view.txtAddress.text = yelpBusiness.businessLocation?.address1

        val city = yelpBusiness.businessLocation?.city
        val state = yelpBusiness.businessLocation?.state

        if(city?.isNotBlank() == true && state?.isNotBlank() == true)
            view.txtCity.text = "$city, $state"
    }
}