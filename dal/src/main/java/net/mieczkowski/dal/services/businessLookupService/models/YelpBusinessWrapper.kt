package net.mieczkowski.dal.services.businessLookupService.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
class YelpBusinessWrapper {

    @JsonProperty("businesses")
    lateinit var YelpBusinesss: ArrayList<YelpBusiness>
}