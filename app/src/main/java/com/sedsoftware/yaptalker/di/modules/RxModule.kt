package com.sedsoftware.yaptalker.di.modules

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.base.events.ConnectionState
import com.sedsoftware.yaptalker.base.navigation.NavigationDrawerItems
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RxModule {

  @Singleton
  @Provides
  fun provideAppbarBus(): BehaviorRelay<String> = BehaviorRelay.createDefault("")

  @Singleton
  @Provides
  @Named("navDrawer")
  fun provideNavDrawerBus(): BehaviorRelay<Long> = BehaviorRelay.createDefault(NavigationDrawerItems.MAIN_PAGE)

  @Singleton
  @Provides
  @Named("connection")
  fun provideConnectionBus(): BehaviorRelay<Long> = BehaviorRelay.createDefault(ConnectionState.IDLE)
}
