package com.sedsoftware.yaptalker.di.modules

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.data.remote.yap.YapLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(RequestsModule::class, RxModule::class))
class DataManagerModule {

  @Provides
  @Singleton
  fun provideYapDataManager(
      yapLoader: YapLoader,
      requestState: BehaviorRelay<Long>) =
      YapDataManager(yapLoader, requestState)
}
