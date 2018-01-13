package com.sedsoftware.yaptalker.di.modules

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.device.settings.SettingsManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

  @Singleton
  @Provides
  fun provideSharedPreferences(ctx: Context): SharedPreferences =
      PreferenceManager.getDefaultSharedPreferences(ctx)

  @Singleton
  @Provides
  fun provideSettingsManager(ctx: Context, prefs: SharedPreferences): Settings =
      SettingsManager(ctx, prefs)
}
