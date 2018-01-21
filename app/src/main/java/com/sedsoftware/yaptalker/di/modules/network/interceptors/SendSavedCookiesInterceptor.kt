package com.sedsoftware.yaptalker.di.modules.network.interceptors

import com.sedsoftware.yaptalker.di.modules.network.cookies.CustomCookieStorage
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class SendSavedCookiesInterceptor(private val cookieStorage: CustomCookieStorage) : Interceptor {

  companion object {
    private const val COOKIE_HEADER = "Cookie"
  }

  override fun intercept(chain: Interceptor.Chain): Response {

    val builder = chain.request().newBuilder()
    val sid = cookieStorage.getFromPrefs()
    Timber.tag("OkHttp").d("+++ Extracted sid from prefs: $sid")

    if (sid.isNotEmpty()) {
      builder.addHeader(COOKIE_HEADER, "SID=$sid")
    }

    return chain.proceed(builder.build())
  }
}
