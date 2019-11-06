package com.sedsoftware.yaptalker.di.module.network

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.sedsoftware.yaptalker.BuildConfig
import com.sedsoftware.yaptalker.di.module.network.interceptors.HeaderAndParamManipulationInterceptor
import com.sedsoftware.yaptalker.di.module.network.interceptors.HtmlFixerInterceptor
import com.sedsoftware.yaptalker.di.module.network.interceptors.SaveReceivedCookiesInterceptor
import com.sedsoftware.yaptalker.di.module.network.interceptors.SendSavedCookiesInterceptor
import com.sedsoftware.yaptalker.domain.device.CookieStorage
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import javax.inject.Named
import javax.inject.Singleton

@Module
class HttpClientsModule {

    private val loggingLevel: Level by lazy {
        if (BuildConfig.DEBUG) Level.HEADERS
        else Level.NONE
    }

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(loggingLevel)
    }

    @Provides
    @Singleton
    fun provideSetCookieCache(): SetCookieCache =
        SetCookieCache()

    @Provides
    @Singleton
    fun provideSharedPrefsCookiePersistor(context: Context): SharedPrefsCookiePersistor =
        SharedPrefsCookiePersistor(context)

    @Provides
    @Singleton
    fun providePersistentCookieJar(
        cache: SetCookieCache,
        persistor: SharedPrefsCookiePersistor
    ): PersistentCookieJar =
        PersistentCookieJar(cache, persistor)

    @Singleton
    @Provides
    @Named("siteClient")
    fun provideSiteClient(cookieStorage: CookieStorage): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HtmlFixerInterceptor())
            .addInterceptor(HeaderAndParamManipulationInterceptor())
            .addInterceptor(SaveReceivedCookiesInterceptor(cookieStorage))
            .addInterceptor(SendSavedCookiesInterceptor(cookieStorage))
            .addInterceptor(loggingInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("apiClient")
    fun provideApiClient(jar: PersistentCookieJar): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(HeaderAndParamManipulationInterceptor())
        builder.addInterceptor(loggingInterceptor)
        builder.cookieJar(jar)
        builder.cache(null)
        return builder.build()
    }

    @Singleton
    @Provides
    @Named("fileClient")
    fun provideFileClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    @Named("outerClient")
    fun provideOuterClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
}
