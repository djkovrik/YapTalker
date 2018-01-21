package com.sedsoftware.yaptalker.di.modules.network.interceptors

import com.sedsoftware.yaptalker.di.modules.network.cookies.CustomCookieStorage
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.ArrayList

class SaveReceivedCookiesInterceptor(private val cookieStorage: CustomCookieStorage) : Interceptor {

  companion object {
    private const val COOKIE_HEADER_MARKER = "Set-Cookie"
  }

  override fun intercept(chain: Interceptor.Chain): Response {

    val originalResponse = chain.proceed(chain.request())
    val currentCookies = cookieStorage.getFromPrefs()
    val newCookies: MutableSet<String> = HashSet()

    newCookies.addAll(currentCookies)

    if (originalResponse.headers(COOKIE_HEADER_MARKER).isNotEmpty()) {
      originalResponse.headers(COOKIE_HEADER_MARKER).forEach { cookie ->
        newCookies.add(cookie)
      }
    }

    cookieStorage.saveToPrefs(newCookies)

    return originalResponse
  }
}
