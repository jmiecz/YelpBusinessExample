package net.mieczkowski.dal.services.authService

import io.reactivex.Single
import net.mieczkowski.dal.services.authService.models.YelpAuth
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
interface AuthContract {

    @FormUrlEncoded
    @POST("oauth2/token")
    fun getYelpAuth(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("grant_type") grantType: String
    ): Single<YelpAuth>
}