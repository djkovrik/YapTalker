package com.sedsoftware.yaptalker.di.modules

import com.sedsoftware.yaptalker.YapTalkerApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val app: YapTalkerApp) {

  @Provides
  @Singleton
  fun provideApplication() = app

}