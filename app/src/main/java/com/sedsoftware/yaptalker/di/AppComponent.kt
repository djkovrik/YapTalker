package com.sedsoftware.yaptalker.di

import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.di.module.AppModule
import com.sedsoftware.yaptalker.di.module.DeviceModule
import com.sedsoftware.yaptalker.di.module.NavigationModule
import com.sedsoftware.yaptalker.di.module.NetworkModule
import com.sedsoftware.yaptalker.di.module.contribution.ActivityContributionModule
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
  modules = [
    (ActivityContributionModule::class),
    (AppModule::class),
    (DeviceModule::class),
    (NavigationModule::class),
    (NetworkModule::class)
  ]
)
interface AppComponent : AndroidInjector<YapTalkerApp> {

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<YapTalkerApp>()
}
