package com.sedsoftware.yaptalker.di.modules.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class SaveReceivedCookiesInterceptor : Interceptor {

  companion object {
    private const val COOKIE_HEADER_MARKER = "Set-Cookie"
  }

  override fun intercept(chain: Interceptor.Chain): Response {

    val originalResponse = chain.proceed(chain.request())

    if (originalResponse.headers(COOKIE_HEADER_MARKER).isNotEmpty()) {
      originalResponse.headers(COOKIE_HEADER_MARKER).forEach { cookie ->
        Timber.d("<<< Received cookie: $cookie")
      }
    }

    return originalResponse
  }
}
