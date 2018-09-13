package net.mieczkowski.dal.client

import okhttp3.OkHttpClient

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
interface ClientContract {

    fun getHttpClient(): OkHttpClient
}