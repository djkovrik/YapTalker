package com.sedsoftware.yaptalker.di.modules

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.data.remote.yap.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(RequestsModule::class, RxModule::class))
class DataManagerModule {

  @Provides
  @Singleton
  fun provideYapDataManager(
      news: YapNewsLoader,
      forums: YapForumsListLoader,
      chosenForum: YapChosenForumLoader,
      chosenTopic: YapChosenTopicLoader,
      requestState: BehaviorRelay<Long>) =
      YapDataManager(news, forums, chosenForum,
          chosenTopic, requestState)
}