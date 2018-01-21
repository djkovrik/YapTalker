package com.sedsoftware.yaptalker.di.modules.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class SendSavedCookiesInterceptor : Interceptor {

  companion object {
    private const val COOKIE_HEADER = "Cookie"
  }

  override fun intercept(chain: Interceptor.Chain): Response {

    val builder = chain.request().newBuilder()

    val cookies = listOf(
      "remote_authorised=deleted; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=/; domain=.yaplakal.com",
      "SID=deleted; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=/; domain=alpha.yaplakal.com"
    )

    cookies.forEach { cookie ->
      Timber.d(">>> Send cookie: $cookie")
      builder.addHeader(COOKIE_HEADER, cookie)
    }

    return chain.proceed(builder.build())
  }
}
