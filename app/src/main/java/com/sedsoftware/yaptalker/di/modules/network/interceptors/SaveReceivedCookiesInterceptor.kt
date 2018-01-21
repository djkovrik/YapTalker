package com.sedsoftware.yaptalker.di.modules.network.interceptors

import com.sedsoftware.yaptalker.di.modules.network.cookies.CustomCookieStorage
import okhttp3.Interceptor
import okhttp3.Response

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
