package com.sedsoftware.yaptalker.di.modules

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.commons.enums.navigation.NavigationSection
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RxModule {

  @Singleton
  @Provides
  fun provideAppbarBus(): BehaviorRelay<String> = BehaviorRelay.createDefault("")

  @Singleton
  @Provides
  fun provideNavDrawerBus(): BehaviorRelay<Long> = BehaviorRelay.createDefault(NavigationSection.MAIN_PAGE)
}
