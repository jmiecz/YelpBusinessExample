package net.mieczkowski.dal

import android.app.Application
import android.os.Build
import net.mieczkowski.dal.client.Client
import net.mieczkowski.dal.client.ClientContract
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.with
import org.koin.dsl.module.module
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.get

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
object DAL : KoinComponent{

    internal val API_URL_KEY = "dalApiUrl"
    internal val VERSION_API_KEY = "dalVersionApi"

    private val BASE_URL = "https://api.yelp.com/"
    private val VERSION = "3"

    private val clientModule = module {
        single { Client(androidContext()) as ClientContract }
    }

    fun init(application: Application) {
        StandAloneContext.loadProperties(extraProperties = mapOf(
                API_URL_KEY to BASE_URL,
                VERSION_API_KEY to "${BASE_URL}v$VERSION/"
        )) with application

        StandAloneContext.loadKoinModules(listOf(clientModule)) with application

        val preLoadClient: ClientContract = get()
    }

}