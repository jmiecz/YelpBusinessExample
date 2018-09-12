package net.mieczkowski.dal

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager
import net.mieczkowski.dal.cache.TokenCacheContract
import net.mieczkowski.dal.cache.TokenCacheService
import net.mieczkowski.dal.client.Client
import net.mieczkowski.dal.client.ClientContract
import net.mieczkowski.dal.services.authService.AuthContract
import net.mieczkowski.dal.services.authService.AuthService
import net.mieczkowski.dal.services.locationService.LocationService
import net.mieczkowski.dal.tools.ServiceChecker
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.get

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
object DAL : KoinComponent {

    private val API_URL_KEY = "dalApiUrl"
    private val VERSION_API_KEY = "dalVersionApi"

    private val BASE_URL = "https://api.yelp.com/"
    private val VERSION = "3"

    private val toolsModule = module {
        single { ServiceChecker(androidContext()) }
        single { LocationService(androidContext()) }
    }

    private val networkModule = module {
        single { Client(androidContext(), get()) as ClientContract }

        single { TokenCacheService() as TokenCacheContract }

        single { RetrofitFactory.createInstance(getProperty(API_URL_KEY)) as AuthContract }
        factory { AuthService(get(), get()) }
    }

    fun startInstance(application: Application) {
        FlowManager.init(application)
        //Stetho.initializeWithDefaults(application)

        StandAloneContext.loadProperties(extraProperties = mapOf(
                API_URL_KEY to BASE_URL,
                VERSION_API_KEY to "${BASE_URL}v$VERSION/"
        ))

        StandAloneContext.loadKoinModules(listOf(
                toolsModule,
                networkModule
        ))

        val preLoadClient: ClientContract = get()
    }

}