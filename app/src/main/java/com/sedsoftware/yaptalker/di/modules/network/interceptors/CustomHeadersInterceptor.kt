package com.sedsoftware.yaptalker.di.modules.network.interceptors

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class CustomHeadersInterceptor : Interceptor {

  companion object {
    private val headers = mapOf(
      "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
      "Accept-Language" to "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3",
      "Connection" to "keep-alive",
      "Host" to "www.yaplakal.com",
      "Referer" to "http://www.yaplakal.com/",
      "User-Agent" to "YapTalker"
    )
  }

  override fun intercept(chain: Chain): Response {
    val builder = chain.request().newBuilder()

    headers.forEach { (parameter, value) ->
      builder.addHeader(parameter, value)
    }

    return chain.proceed(builder.build())
  }
}
