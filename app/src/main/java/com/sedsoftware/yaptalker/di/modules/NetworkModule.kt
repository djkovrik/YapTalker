package com.sedsoftware.yaptalker.di.modules

import com.sedsoftware.yaptalker.data.network.YapLoader
import com.sedsoftware.yaptalker.di.modules.network.HttpClientsModule
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [(HttpClientsModule::class)])
class NetworkModule {

  companion object {
    private const val SITE_ENDPOINT = "https://www.yaplakal.com/"
  }

  @Singleton
  @Provides
  fun provideYapLoader(@Named("siteClient") okHttpClient: OkHttpClient): YapLoader =
      Retrofit
          .Builder()
          .baseUrl(SITE_ENDPOINT)
          .client(okHttpClient)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(JspoonConverterFactory.create())
          .addConverterFactory(ScalarsConverterFactory.create())
          .build()
          .create(YapLoader::class.java)
}
