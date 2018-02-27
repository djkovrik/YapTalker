package com.sedsoftware.yaptalker.di.modules.contribution

import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.presentation.features.gallery.TopicGalleryActivity
import com.sedsoftware.yaptalker.presentation.features.gallery.di.TopicGalleryActivityModule
import com.sedsoftware.yaptalker.presentation.features.gifdisplay.GifDisplayActivity
import com.sedsoftware.yaptalker.presentation.features.gifdisplay.di.GifDisplayActivityModule
import com.sedsoftware.yaptalker.presentation.features.imagedisplay.ImageDisplayActivity
import com.sedsoftware.yaptalker.presentation.features.imagedisplay.di.ImageDisplayActivityModule
import com.sedsoftware.yaptalker.presentation.features.navigation.MainActivity
import com.sedsoftware.yaptalker.presentation.features.navigation.di.MainActivityModule
import com.sedsoftware.yaptalker.presentation.features.settings.SettingsActivity
import com.sedsoftware.yaptalker.presentation.features.settings.di.SettingsActivityModule
import com.sedsoftware.yaptalker.presentation.features.videodisplay.VideoDisplayActivity
import com.sedsoftware.yaptalker.presentation.features.videodisplay.di.VideoDisplayActivityModule
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

  @ActivityScope
  @ContributesAndroidInjector(modules = [(VideoDisplayActivityModule::class)])
  fun videoActivityInjector(): VideoDisplayActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [(GifDisplayActivityModule::class)])
  fun gifActivityInjector(): GifDisplayActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [(TopicGalleryActivityModule::class)])
  fun galleryActivityInjector(): TopicGalleryActivity
}
