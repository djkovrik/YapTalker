package com.sedsoftware.yaptalker.data.remote

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import okhttp3.OkHttpClient

val requestsClientModule = Kodein.Module {

  // Site requests client instance
  bind<OkHttpClient>("siteClient") with singleton {
    OkHttpClient.Builder()
        .cookieJar(instance())
        .build()
  }

  // File download client instance
  bind<OkHttpClient>("fileClient") with singleton { OkHttpClient() }
}
