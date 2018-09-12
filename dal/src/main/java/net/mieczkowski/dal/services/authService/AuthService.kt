package net.mieczkowski.dal.services.authService

import io.reactivex.Single
import net.mieczkowski.dal.cache.TokenCacheContract
import net.mieczkowski.dal.services.authService.models.YelpAuth
import net.mieczkowski.dal.services.authService.models.YelpAuthRequest

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
class AuthService(private val authContract: AuthContract): TokenCacheContract {

    override fun getCacheToken(): YelpAuth? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getYelpAuth(): Single<YelpAuth>{
        getCacheToken()?.let { return Single.just(it) }

        return getNewYelpAuth()
    }

    private fun getNewYelpAuth(): Single<YelpAuth> =
            YelpAuthRequest().let {
                authContract.getYelpAuth(it.clientID, it.clientSecret, it.grantType)
                        .doOnSuccess { yelpAuth ->
                            clearToken()

                            //TODO save yelpAuth
                        }
            }

    private fun clearToken(){

    }
}