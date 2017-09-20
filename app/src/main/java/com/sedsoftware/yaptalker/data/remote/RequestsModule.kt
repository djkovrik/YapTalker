package com.sedsoftware.yaptalker.data.remote

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.github.salomonbrys.kodein.with
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val requestsModule = Kodein.Module {

  constant("YAP_ENDPOINT") with "http://www.yaplakal.com/"
  constant("COUB_ENDPOINT") with "http://coub.com/"
  constant("RUTUBE_ENDPOINT") with "http://rutube.ru/"
  constant("YAP_VIDEO_ENDPOINT") with "http://api.yapfiles.ru/"

  // Retrofit bindings
  bind<YapLoader>("YapLoader") with singleton {
    Retrofit.Builder()
        .baseUrl(instance<String>("YAP_ENDPOINT"))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JspoonConverterFactory.create())
        .build()
        .create(YapLoader::class.java)
  }

  bind<CoubLoader>("CoubLoader") with singleton {
    Retrofit.Builder()
        .baseUrl(instance<String>("COUB_ENDPOINT"))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(CoubLoader::class.java)
  }

  bind<RutubeLoader>("RutubeLoader") with singleton {
    Retrofit.Builder()
        .baseUrl(instance<String>("RUTUBE_ENDPOINT"))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(RutubeLoader::class.java)
  }

  bind<YapPlayerLoader>("YapPlayerLoader") with singleton {
    Retrofit.Builder()
        .baseUrl(instance<String>("YAP_VIDEO_ENDPOINT"))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(YapPlayerLoader::class.java)
  }

  // OkHttp client for file downloading
  bind<OkHttpClient>() with singleton { OkHttpClient() }
}
