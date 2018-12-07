package com.sedsoftware.yaptalker.di.module.network

import com.sedsoftware.yaptalker.BuildConfig
import com.sedsoftware.yaptalker.di.module.network.interceptors.CustomHeadersInterceptor
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

    @Singleton
    @Provides
    @Named("siteClient")
    fun provideSiteClient(cookieStorage: CookieStorage): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HtmlFixerInterceptor())
            .addInterceptor(CustomHeadersInterceptor())
            .addInterceptor(SaveReceivedCookiesInterceptor(cookieStorage))
            .addInterceptor(SendSavedCookiesInterceptor(cookieStorage))
            .addInterceptor(loggingInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("fileClient")
    fun provideFileClient(): OkHttpClient = OkHttpClient.Builder().build()
}
