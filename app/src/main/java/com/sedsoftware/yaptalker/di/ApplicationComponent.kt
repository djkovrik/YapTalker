package com.sedsoftware.yaptalker.di

import com.sedsoftware.yaptalker.di.modules.ApplicationModule
import com.sedsoftware.yaptalker.di.modules.DataManagerModule
import com.sedsoftware.yaptalker.features.navigation.NavigationViewPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, DataManagerModule::class))
interface ApplicationComponent {
  // Injections here
  fun inject(presenter: NavigationViewPresenter)
}