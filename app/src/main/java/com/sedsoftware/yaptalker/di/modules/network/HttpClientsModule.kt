package com.sedsoftware.yaptalker.di.modules.network

import dagger.Module
import dagger.Provides
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [(CookiesModule::class)])
class HttpClientsModule {

  @Singleton
  @Provides
  @Named("siteClient")
  fun provideSiteClient(jar: CookieJar): OkHttpClient =
      OkHttpClient
          .Builder()
          .cookieJar(jar)
          .addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader("User-Agent", "YapTalker").build()
            chain.proceed(request)
          }
          .build()

  @Singleton
  @Provides
  @Named("fileClient")
  fun provideFileClient(): OkHttpClient = OkHttpClient()
}
