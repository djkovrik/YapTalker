package com.sedsoftware.yaptalker.di.modules

import dagger.Module
import dagger.Provides
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {

  companion object {
    val YAPLAKAL_ENDPOINT = "http://www.yaplakal.com/"
    val RUTUBE_ENDPOINT = "http://rutube.ru/"
    val COUB_ENDPOINT = "http://coub.com/"
  }

  @Provides
  @Singleton
  @Named("YapLoader")
  fun provideRetrofitYapLoader(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(YAPLAKAL_ENDPOINT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JspoonConverterFactory.create())
        .build()
  }

  @Provides
  @Singleton
  @Named("RutubeThumbnailLoader")
  fun provideRetrofitRutubeThumbnail(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(RUTUBE_ENDPOINT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
  }

  @Provides
  @Singleton
  @Named("CoubThumbnailLoader")
  fun provideRetrofitCoubThumbnail(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(COUB_ENDPOINT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
  }
}
