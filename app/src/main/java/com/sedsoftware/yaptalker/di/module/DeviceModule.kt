package com.sedsoftware.yaptalker.di.module

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import com.sedsoftware.yaptalker.device.cookies.YapCookieStorage
import com.sedsoftware.yaptalker.device.settings.SettingsManager
import com.sedsoftware.yaptalker.domain.device.CookieStorage
import com.sedsoftware.yaptalker.domain.device.Settings
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(ctx: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(ctx)

    @Singleton
    @Provides
    fun provideSettings(ctx: Context, prefs: SharedPreferences): Settings =
        SettingsManager(ctx, prefs)

    @Singleton
    @Provides
    fun provideCookieStorage(settings: Settings): CookieStorage =
        YapCookieStorage(settings)
}
