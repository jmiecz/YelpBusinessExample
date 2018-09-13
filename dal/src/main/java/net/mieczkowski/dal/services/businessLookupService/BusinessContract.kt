package net.mieczkowski.dal.services.businessLookupService

import io.reactivex.Single
import net.mieczkowski.dal.services.businessLookupService.models.BusinessDetails
import net.mieczkowski.dal.services.businessLookupService.models.YelpBusinessWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
interface BusinessContract {

    @GET("businesses/search")
    abstract fun lookUpBusiness(@Query("term") search: String,
                                @Query("latitude") latitude: Double,
                                @Query("longitude") longitude: Double): Single<YelpBusinessWrapper>

    @GET("businesses/{id}")
    abstract fun getBusinessDetails(@Path("id") businessID: String): Single<BusinessDetails>
}