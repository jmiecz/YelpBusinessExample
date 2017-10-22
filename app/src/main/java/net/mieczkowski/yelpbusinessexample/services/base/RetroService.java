package net.mieczkowski.yelpbusinessexample.services.base;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.mieczkowski.yelpbusinessexample.models.auth.YelpAuth;
import net.mieczkowski.yelpbusinessexample.services.YelpAuthService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class RetroService<I> {

    private static OkHttpClient okHttpClient;
    private static ObjectMapper objectMapper;

    private I retroInterface;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setTimeZone(TimeZone.getDefault());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> ArrayList<T> getArrayList(String json, Class<T> toConvertTo) throws IOException {
        return RetroService.getObjectMapper().readValue(
                json,
                RetroService.getObjectMapper().getTypeFactory().constructCollectionType(
                        ArrayList.class, toConvertTo
                ));
    }

    public RetroService(Class<I> clazz, String baseUrl, boolean useCacheOkHttp) {
        init(clazz, baseUrl, useCacheOkHttp);
    }

    public RetroService(Class<I> clazz, String baseUrl) {
        init(clazz, baseUrl, true);
    }

    public I getRetroInterface() {
        return retroInterface;
    }

    private void init(Class<I> clazz, String baseUrl, boolean useCacheOkHttp) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(
                        useCacheOkHttp ? getCachedOkHttpClient() : getOkHttpClient(getInterceptor())
                );

        Retrofit retrofit = builder.build();

        retroInterface = retrofit.create(clazz);
    }

    private boolean allowLogging() {
        return true;
    }

    private Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .method(original.method(), original.body());

                addToRequestBuilder(requestBuilder);

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        };
    }

    private void addToRequestBuilder(Request.Builder builder) {
        YelpAuth yelpAuth = new YelpAuthService().getCacheYelpAuth();

        if (yelpAuth != null) {
            builder.header("Authorization", "Bearer " + yelpAuth.getAccessToken());
        }
    }

    private OkHttpClient getCachedOkHttpClient() {
        if (okHttpClient != null) {
            return okHttpClient;
        }

        okHttpClient = getOkHttpClient(getInterceptor());

        return okHttpClient;
    }

    private OkHttpClient getOkHttpClient(Interceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        if (allowLogging()) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        builder.addInterceptor(interceptor);

        return builder.build();
    }

}
