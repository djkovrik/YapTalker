package com.sedsoftware.yaptalker.di.modules.network.interceptors

import com.sedsoftware.yaptalker.di.modules.network.cookies.CustomCookieStorage
import okhttp3.Interceptor
import okhttp3.Response

class SendSavedCookiesInterceptor(private val cookieStorage: CustomCookieStorage) : Interceptor {

  companion object {
    private const val COOKIE_HEADER = "Cookie"
  }

  override fun intercept(chain: Interceptor.Chain): Response {

    val builder = chain.request().newBuilder()
    val cookies = cookieStorage.getFromPrefs()

    cookies.forEach { cookie ->
      builder.addHeader(COOKIE_HEADER, cookie)
    }

    return chain.proceed(builder.build())
  }
}
