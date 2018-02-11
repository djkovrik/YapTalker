package com.sedsoftware.yaptalker.di.modules

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.presentation.base.events.AppEvent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RxModule {

  @Singleton
  @Provides
  fun provideEventBus(): BehaviorRelay<AppEvent> = BehaviorRelay.create()
}
