package net.mieczkowski.dal.cache

import net.mieczkowski.dal.services.authService.models.YelpAuth

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
interface TokenCacheContract {

    fun getCacheToken(): YelpAuth?

}