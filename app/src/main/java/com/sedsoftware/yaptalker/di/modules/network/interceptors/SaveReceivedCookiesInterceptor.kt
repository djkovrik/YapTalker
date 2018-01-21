package com.sedsoftware.yaptalker.di.modules.network.interceptors

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.ArrayList

class SaveReceivedCookiesInterceptor : Interceptor {

  companion object {
    private const val COOKIE_HEADER_MARKER = "Set-Cookie"
  }

  override fun intercept(chain: Interceptor.Chain): Response {

    val originalResponse = chain.proceed(chain.request())
    val cookiesList = ArrayList<String>()

    if (originalResponse.headers(COOKIE_HEADER_MARKER).isNotEmpty()) {
      originalResponse.headers(COOKIE_HEADER_MARKER).forEach { cookie ->
        Timber.d("<<< Received cookie: $cookie")
        cookiesList.add(cookie)
      }
    }

    val moshi = Moshi.Builder().build()
    val jsonAdapter: JsonAdapter<List<String>> =
      moshi.adapter(Types.newParameterizedType(List::class.java, String::class.java))

    val json = jsonAdapter.toJson(cookiesList)

    Timber.d("+++ Resulting json: $json")

    val restoredCookies = jsonAdapter.fromJson(json)

    restoredCookies?.forEach { Timber.d("!!! Restored cookie: $it") }

    return originalResponse
  }
}
