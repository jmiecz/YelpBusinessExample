package net.mieczkowski.dal.services.authService.models

import net.mieczkowski.dal.BuildConfig

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
class YelpAuthRequest {

    val grantType = "client_credentials"

    val clientID = "" //BuildConfig.YELP_CLIENT_ID

    val clientSecret = "" //BuildConfig.YELP_CLIENT_SECRET
}