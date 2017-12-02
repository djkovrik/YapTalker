package com.sedsoftware.yaptalker.di.modules

import com.sedsoftware.yaptalker.di.modules.presentation.navigation.MainActivityModule
import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.presentation.navigation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [(AndroidSupportInjectionModule::class)])
interface ActivityContributionModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
  fun mainActivityInjector(): MainActivity
}
