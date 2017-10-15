package com.sedsoftware.yaptalker.data.remote

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.github.salomonbrys.kodein.with
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.features.base.events.ConnectionState
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val yapDataManagerModule = Kodein.Module {

  constant("YAP_SITE_ENDPOINT") with "http://www.yaplakal.com/"

  bind<YapLoader>("YapLoader") with singleton {
    Retrofit.Builder()
        .baseUrl(instance<String>("YAP_SITE_ENDPOINT"))
        .client(instance("siteClient"))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JspoonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(YapLoader::class.java)
  }

  // Rx bus for connection events
  bind<BehaviorRelay<Long>>() with singleton { BehaviorRelay.createDefault(ConnectionState.IDLE) }

  bind<YapDataManager>() with singleton {
    YapDataManager(instance("YapLoader"), instance())
  }
}
