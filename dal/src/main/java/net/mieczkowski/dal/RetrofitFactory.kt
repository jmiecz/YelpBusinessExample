package net.mieczkowski.dal

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import net.mieczkowski.dal.client.ClientContract
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
internal object RetrofitFactory : KoinComponent {

    val client: ClientContract by inject()

    val objectMapper = ObjectMapper().apply {
        registerKotlinModule()
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    inline fun <reified T> createInstance(baseUrl: String): T =
            Retrofit.Builder()
                    .client(client.getHttpClient())
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                    .build().create(T::class.java)
}