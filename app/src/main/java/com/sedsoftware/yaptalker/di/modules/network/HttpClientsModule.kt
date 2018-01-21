package com.sedsoftware.yaptalker.di.modules.network

import com.sedsoftware.yaptalker.BuildConfig
import com.sedsoftware.yaptalker.di.modules.network.cookies.CookiesModule
import com.sedsoftware.yaptalker.di.modules.network.cookies.CustomCookieStorage
import com.sedsoftware.yaptalker.di.modules.network.interceptors.CustomHeadersInterceptor
import com.sedsoftware.yaptalker.di.modules.network.interceptors.SaveReceivedCookiesInterceptor
import com.sedsoftware.yaptalker.di.modules.network.interceptors.SendSavedCookiesInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [(CookiesModule::class)])
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
  fun provideSiteClient(cookieStorage: CustomCookieStorage): OkHttpClient =
    OkHttpClient
      .Builder()
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
