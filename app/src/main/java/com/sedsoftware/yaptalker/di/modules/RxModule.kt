package com.sedsoftware.yaptalker.di.modules

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.data.remote.yap.YapRequestState
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RxModule {

  @Provides
  @Singleton
  fun provideStateBehaviorRelay() : BehaviorRelay<Long> {
    return BehaviorRelay.createDefault(YapRequestState.IDLE)
  }

  @Provides
  @Singleton
  fun provideTitleBehaviorRelay() : BehaviorRelay<String> {
    return BehaviorRelay.createDefault("")
  }
}
