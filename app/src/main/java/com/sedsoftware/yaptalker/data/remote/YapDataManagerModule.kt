package com.sedsoftware.yaptalker.data.remote

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.github.salomonbrys.kodein.with
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

val yapDataManagerModule = Kodein.Module {

  constant("YAP_SITE_ENDPOINT") with "http://www.yaplakal.com/"

  bind<YapLoader>("YapLoader") with singleton {
    Retrofit.Builder()
        .baseUrl(instance<String>("YAP_SITE_ENDPOINT"))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JspoonConverterFactory.create())
        .build()
        .create(YapLoader::class.java)
  }

  bind<YapDataManager>() with singleton {
    YapDataManager(instance("YapLoader"), instance())
  }
}
