package com.sedsoftware.yaptalker.data

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.github.salomonbrys.kodein.with
import com.sedsoftware.yaptalker.commons.converters.HashSearchConverterFactory
import com.sedsoftware.yaptalker.data.requests.site.YapLoader
import com.sedsoftware.yaptalker.data.requests.site.YapSearchIdLoader
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val yapDataManagerModule = Kodein.Module {

  constant("YAP_SITE_ENDPOINT") with "http://www.yaplakal.com/"
  constant("SEARCH_ID_HASH_MARKER") with "searchid="

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

  bind<YapSearchIdLoader>("YapSearchIdLoader") with singleton {
    Retrofit.Builder()
        .baseUrl(instance<String>("YAP_SITE_ENDPOINT"))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(HashSearchConverterFactory.create(instance("SEARCH_ID_HASH_MARKER")))
        .build()
        .create(YapSearchIdLoader::class.java)
  }

  bind<YapDataManager>() with singleton {
    YapDataManager(instance("YapLoader"), instance("YapSearchIdLoader"), instance("connectionState"))
  }
}
