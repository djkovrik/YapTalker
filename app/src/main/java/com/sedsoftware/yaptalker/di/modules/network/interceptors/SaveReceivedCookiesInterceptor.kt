package com.sedsoftware.yaptalker.di.modules.network.interceptors

import com.sedsoftware.yaptalker.di.modules.network.cookies.CustomCookieStorage
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class SaveReceivedCookiesInterceptor(private val cookieStorage: CustomCookieStorage) : Interceptor {

  companion object {
    private const val COOKIE_HEADER_MARKER = "Set-Cookie"
    private const val SID_MARKER = "SID"
    private const val DELETED_MARKER = "deleted"
  }

  override fun intercept(chain: Interceptor.Chain): Response {

    val originalResponse = chain.proceed(chain.request())
    val cookies = originalResponse.headers(COOKIE_HEADER_MARKER)

    cookies.forEach { cookie ->
      if (cookie.contains(SID_MARKER)) {
        Timber.tag("OkHttp").d("-- SID detected!")
        val sid = cookie.substringAfter("=").substringBefore(";")
        Timber.tag("OkHttp").d("-- Extracted sid = $sid")
        if (sid != DELETED_MARKER) {
          cookieStorage.saveToPrefs(sid)
          Timber.tag("OkHttp").d("-- Saving $sid as main sid")
        }
      }
    }

    return originalResponse
  }
}
