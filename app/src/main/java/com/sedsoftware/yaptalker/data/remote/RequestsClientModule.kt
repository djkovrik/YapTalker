package com.sedsoftware.yaptalker.data.remote

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.CookieCache
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import okhttp3.CookieJar
import okhttp3.OkHttpClient

val requestsClientModule = Kodein.Module {

  bind<CookieCache>() with singleton { SetCookieCache() }

  bind<CookiePersistor>() with singleton { SharedPrefsCookiePersistor(instance<Context>()) }

  bind<CookieJar>() with singleton { PersistentCookieJar(instance(), instance()) }

  // Site requests client instance
  bind<OkHttpClient>("siteClient") with singleton {
    OkHttpClient.Builder()
        .cookieJar(instance())
        .build()
  }

  // File download client instance
  bind<OkHttpClient>("fileClient") with singleton { OkHttpClient() }
}
