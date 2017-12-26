package com.sedsoftware.yaptalker.di.modules

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

  @Singleton
  @Provides
  fun provideSharedPreferences(ctx: Context): SharedPreferences =
      PreferenceManager.getDefaultSharedPreferences(ctx)
}
