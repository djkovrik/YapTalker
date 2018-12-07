package com.sedsoftware.yaptalker.di.module

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sedsoftware.yaptalker.device.cookies.YapCookieStorage
import com.sedsoftware.yaptalker.device.settings.SettingsManager
import com.sedsoftware.yaptalker.device.storage.state.TopicStateStorage
import com.sedsoftware.yaptalker.domain.device.CookieStorage
import com.sedsoftware.yaptalker.domain.device.Settings
import dagger.Module
import dagger.Provides

@Module
class DeviceModule {

    @Provides
    fun provideSettings(ctx: Context, prefs: SharedPreferences): Settings =
        SettingsManager(ctx, prefs)

    @Provides
    fun provideCookieStorage(settings: Settings): CookieStorage =
        YapCookieStorage(settings)

    @Provides
    fun provideTopicStateStorage(gson: Gson, preferences: SharedPreferences): TopicStateStorage =
        TopicStateStorage(gson, preferences)
}
