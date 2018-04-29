package com.sedsoftware.yaptalker.di.module

import android.app.Application
import android.content.Context
import com.sedsoftware.yaptalker.YapTalkerApp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Module(includes = [(AndroidInjectionModule::class)])
abstract class AppModule {

  @Module
  companion object {

    @Provides
    @Singleton
    @JvmStatic
    fun provideContext(app: YapTalkerApp): Context = app
  }

  @Binds
  abstract fun application(app: YapTalkerApp): Application
}
