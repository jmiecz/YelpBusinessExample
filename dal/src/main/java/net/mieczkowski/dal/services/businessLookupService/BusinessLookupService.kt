package net.mieczkowski.dal.services.businessLookupService

import com.raizlabs.android.dbflow.sql.language.SQLite
import io.reactivex.Flowable
import io.reactivex.Single
import net.mieczkowski.dal.exts.subscribeOnIO
import net.mieczkowski.dal.services.businessLookupService.models.BusinessLookupRequest
import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness
import net.mieczkowski.dal.services.businessLookupService.models.YelpBusiness_Table
import net.mieczkowski.dal.tools.ServiceChecker

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
class BusinessLookupService(private val businessContract: BusinessContract, private val serviceChecker: ServiceChecker) {

    fun cacheLookUpByName(searchTerm: String): Single<List<YelpBusiness>> =
            Single.create {
                val data = SQLite.select()
                        .from(YelpBusiness::class.java)
                        .where(YelpBusiness_Table.searchKey.`is`(searchTerm))
                        .queryList()

                it.onSuccess(data)
            }

    fun lookUpByName(businessLookupRequest: BusinessLookupRequest): Single<List<YelpBusiness>>{
        if(!serviceChecker.hasInternetAccess()) return cacheLookUpByName(businessLookupRequest.searchTerm)

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
                    }.doOnSuccess { businesses ->
                        //businesses.forEach { it.save() }
                    }.subscribeOnIO()


    private fun getYelpBusinessWithDetails(yelpBusiness: YelpBusiness): Single<YelpBusiness> =
            businessContract.getBusinessDetails(yelpBusiness.id)
                    .map {
                        yelpBusiness.businessDetails = it
                        yelpBusiness
                    }.subscribeOnIO()

    private fun sortYelpBusinesses(yelpBusinesses: List<YelpBusiness>){
        yelpBusinesses.sortedBy { it.businessDetails?.reviewCount }
        yelpBusinesses.sortedBy { it.businessDetails?.rating }
    }
}