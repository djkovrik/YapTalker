package com.sedsoftware.yaptalker.di.modules

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.commons.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.data.event.AppEvent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RxModule {

  @Singleton
  @Provides
  fun provideEventBus(): BehaviorRelay<AppEvent> = BehaviorRelay.create()
}
