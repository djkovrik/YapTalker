package com.sedsoftware.yaptalker.di.modules.network.interceptors

import com.sedsoftware.yaptalker.domain.device.CookieStorage
import okhttp3.Interceptor
import okhttp3.Response

class SendSavedCookiesInterceptor(private val cookieStorage: CookieStorage) : Interceptor {

  companion object {
    private const val COOKIE_HEADER = "Cookie"
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val builder = chain.request().newBuilder()
    val sidCookie = cookieStorage.getCookie()

    if (sidCookie.isNotEmpty()) {
      builder.addHeader(COOKIE_HEADER, sidCookie)
    }

    return chain.proceed(builder.build())
  }
}
