package com.sedsoftware.yaptalker.di

import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.di.modules.AppModule
import com.sedsoftware.yaptalker.di.modules.NavigationModule
import com.sedsoftware.yaptalker.di.modules.NetworkModule
import com.sedsoftware.yaptalker.di.modules.RxModule
import com.sedsoftware.yaptalker.di.modules.SettingsModule
import com.sedsoftware.yaptalker.di.modules.contribution.ActivityContributionModule
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
  modules = [
    (ActivityContributionModule::class),
    (AppModule::class),
    (NavigationModule::class),
    (NetworkModule::class),
    (SettingsModule::class),
    (RxModule::class)
  ]
)
interface AppComponent : AndroidInjector<YapTalkerApp> {

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<YapTalkerApp>()
}
