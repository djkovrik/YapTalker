package com.sedsoftware.yaptalker.data.requests

import android.content.Context
import com.franmontiel.persistentcookiejar.ClearableCookieJar
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

val requestsCookieModule = Kodein.Module {

  bind<CookieCache>() with singleton { SetCookieCache() }

  bind<CookiePersistor>() with singleton { SharedPrefsCookiePersistor(instance<Context>()) }

  bind<CookieJar>() with singleton { PersistentCookieJar(instance(), instance()) }

  bind<ClearableCookieJar>() with singleton { PersistentCookieJar(instance(), instance()) }
}
