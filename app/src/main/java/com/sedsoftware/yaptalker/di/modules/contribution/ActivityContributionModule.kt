package com.sedsoftware.yaptalker.di.modules.contribution

import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.presentation.features.imagedisplay.ImageDisplayActivity
import com.sedsoftware.yaptalker.presentation.features.imagedisplay.di.ImageDisplayActivityModule
import com.sedsoftware.yaptalker.presentation.features.navigation.MainActivity
import com.sedsoftware.yaptalker.presentation.features.navigation.di.MainActivityModule
import com.sedsoftware.yaptalker.presentation.features.settings.SettingsActivity
import com.sedsoftware.yaptalker.presentation.features.settings.di.SettingsActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [(AndroidSupportInjectionModule::class)])
interface ActivityContributionModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
  fun mainActivityInjector(): MainActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [(SettingsActivityModule::class)])
  fun settingsActivityInjector(): SettingsActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [(ImageDisplayActivityModule::class)])
  fun imageActivityInjector(): ImageDisplayActivity
}
