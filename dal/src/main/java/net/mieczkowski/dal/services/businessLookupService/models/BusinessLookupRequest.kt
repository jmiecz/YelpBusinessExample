package net.mieczkowski.dal.services.businessLookupService.models

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
data class BusinessLookupRequest(val searchTerm: String, val location: MyLocation) {
}