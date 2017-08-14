package com.sedsoftware.yaptalker.di.modules

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.data.remote.yap.YapChosenForumLoader
import com.sedsoftware.yaptalker.data.remote.yap.YapChosenTopicLoader
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.data.remote.yap.YapForumsListLoader
import com.sedsoftware.yaptalker.data.remote.yap.YapNewsLoader
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
