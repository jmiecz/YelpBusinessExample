package net.mieczkowski.dal.client

import android.content.Context
import net.mieczkowski.dal.cache.TokenCacheContract
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
class Client(context: Context, private val tokenCacheContract: TokenCacheContract, private val timeOutInSecs: Long = 30L, private val cacheSizeInMB: Long = 100 * 1024 * 1024) : ClientContract {

    private val okHttpClient = OkHttpClient.Builder()
            .apply {
                cache(Cache(context.cacheDir, cacheSizeInMB))
                readTimeout(timeOutInSecs, TimeUnit.SECONDS)
                addInterceptor {chain ->
                    val builder = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")

                    tokenCacheContract.getCacheToken()?.let { builder.addHeader("Authorization", "Bearer $it") }

                    chain.proceed(builder.build())
                }
                //addNetworkInterceptor(StethoInterceptor())
            }
            .build()


    override fun getHttpClient(): OkHttpClient {
        return okHttpClient
    }
}