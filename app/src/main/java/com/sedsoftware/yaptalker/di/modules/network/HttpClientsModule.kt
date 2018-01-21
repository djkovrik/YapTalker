package com.sedsoftware.yaptalker.di.modules.network

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

@Module
class HttpClientsModule {

  private val loggingInterceptor: HttpLoggingInterceptor by lazy {
    HttpLoggingInterceptor().setLevel(Level.HEADERS)
  }

  @Singleton
  @Provides
  @Named("siteClient")
  fun provideSiteClient(): OkHttpClient =
    OkHttpClient
      .Builder()
      .addInterceptor(CustomHeadersInterceptor())
      .addInterceptor(SaveReceivedCookiesInterceptor())
      .addInterceptor(SendSavedCookiesInterceptor())
      .addInterceptor(loggingInterceptor)
      .build()

  @Singleton
  @Provides
  @Named("fileClient")
  fun provideFileClient(): OkHttpClient = OkHttpClient.Builder().build()
}
