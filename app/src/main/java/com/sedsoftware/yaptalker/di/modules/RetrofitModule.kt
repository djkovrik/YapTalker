package com.sedsoftware.yaptalker.di.modules

import com.sedsoftware.yaptalker.data.remote.yap.converters.ChosenForumConverterFactory
import com.sedsoftware.yaptalker.data.remote.yap.converters.ChosenTopicConverterFactory
import com.sedsoftware.yaptalker.data.remote.yap.converters.ForumsListConverterFactory
import com.sedsoftware.yaptalker.data.remote.yap.converters.NewsConverterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {

  companion object {
    val ENDPOINT = "http://www.yaplakal.com/"
    val RUTUBE_ENDPOINT = "http://rutube.ru/"
    val COUB_ENDPOINT = "http://coub.com/"
  }

  @Provides
  @Singleton
  @Named("NewsLoader")
  fun provideRetrofitNewsLoader(): Retrofit {

    return Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(NewsConverterFactory.create())
        .build()
  }

  @Provides
  @Singleton
  @Named("ForumsListLoader")
  fun provideRetrofitForumsListLoader(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ForumsListConverterFactory.create())
        .build()
  }

  @Provides
  @Singleton
  @Named("ChosenForumLoader")
  fun provideRetrofitChosenForumLoader(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ChosenForumConverterFactory.create())
        .build()
  }

  @Provides
  @Singleton
  @Named("ChosenTopicLoader")
  fun provideRetrofitChosenTopicLoader(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ChosenTopicConverterFactory.create())
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
