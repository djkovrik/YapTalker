package com.sedsoftware.yaptalker.di.modules

import com.sedsoftware.yaptalker.data.remote.converters.ChosenForumConverterFactory
import com.sedsoftware.yaptalker.data.remote.converters.ChosenTopicConverterFactory
import com.sedsoftware.yaptalker.data.remote.converters.ForumsListConverterFactory
import com.sedsoftware.yaptalker.data.remote.converters.NewsConverterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {

  companion object {
    val ENDPOINT = "http://www.yaplakal.com/"
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
}