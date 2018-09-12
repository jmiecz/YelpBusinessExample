package net.mieczkowski.dal.services.businessLookupService

import io.reactivex.Flowable
import io.reactivex.Single
import net.mieczkowski.dal.services.businessLookupService.models.BusinessLookupRequest
import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
class BusinessLookupService(private val businessContract: BusinessContract) {

    fun cacheLookUpByName(searchTerm: String): Single<List<YelpBusiness>> =
            Single.create {
//                SQLite.select()
//                        .from(YelpBusiness::class.java)
//                        .where(YelpBusiness_Table.searchKey.`is`(searchTerm))
//                        .queryList()
                TODO()
            }

    fun lookUpByName(businessLookupRequest: BusinessLookupRequest): Single<List<YelpBusiness>>{
        TODO("Need to check for internet")

        return newLookUpByName(businessLookupRequest)
    }

    private fun newLookUpByName(businessLookupRequest: BusinessLookupRequest): Single<List<YelpBusiness>> =
            businessContract.lookUpBusiness(businessLookupRequest.searchTerm, businessLookupRequest.location.latitude, businessLookupRequest.location.longitude)
                    .map { it.yelpBusinesses }
                    .toFlowable().flatMap { Flowable.fromIterable(it) }
                    .flatMap { getYelpBusinessWithDetails(it).toFlowable() }
                    .toList().map { oldList ->
                        val toReturn = mutableListOf<YelpBusiness>()
                        toReturn.addAll(oldList)

                        toReturn.forEach { it.searchKey = businessLookupRequest.searchTerm }

                        toReturn
                    }.map {
                        sortYelpBusinesses(it)

                        it.toList()
                    }.doOnSuccess {
//                        for (yelpBusiness in yelpBusinesses) {
//                            yelpBusiness.save()
//                        }
                        TODO("cache data")
                    }


    private fun getYelpBusinessWithDetails(yelpBusiness: YelpBusiness): Single<YelpBusiness> =
            businessContract.getBusinessDetails(yelpBusiness.id)
                    .map {
                        yelpBusiness.businessDetails = it
                        yelpBusiness
                    }

    private fun sortYelpBusinesses(yelpBusinesses: List<YelpBusiness>){
        yelpBusinesses.sortedBy { it.businessDetails?.reviewCount }
        yelpBusinesses.sortedBy { it.businessDetails?.rating }
    }
}