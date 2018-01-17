package com.sedsoftware.yaptalker.di.modules.network

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.CookieCache
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import dagger.Module
import dagger.Provides
import okhttp3.CookieJar
import javax.inject.Singleton

@Module
class CookiesModule {

  @Singleton
  @Provides
  fun provideCookieCache(): CookieCache = SetCookieCache()

  @Singleton
  @Provides
  fun provideCookiePersistor(ctx: Context): CookiePersistor = SharedPrefsCookiePersistor(ctx)

  @Singleton
  @Provides
  fun provideCookieJar(cache: CookieCache, persistor: CookiePersistor): CookieJar =
    PersistentCookieJar(cache, persistor)
}
