package com.sedsoftware.yaptalker.di.modules.network.cookies

import com.sedsoftware.yaptalker.domain.device.Settings
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class CustomCookieStorage(private val settings: Settings) {

  private val moshi: Moshi by lazy {
    Moshi.Builder().build()
  }

  private val jsonAdapter: JsonAdapter<Set<String>> by lazy {
    moshi.adapter<Set<String>>(Types.newParameterizedType(Set::class.java, String::class.java))
  }

  fun saveToPrefs(cookies: Set<String>) {
    toJson(cookies).also { jsonForSaving -> settings.saveCookies(jsonForSaving) }
  }

  fun getFromPrefs(): Set<String> {

    val cookies = HashSet<String>()
    val storedCookiesJson = settings.getCookies()

    if (storedCookiesJson.isEmpty()) {
      return cookies
    }

    val storedCookies = fromJson(storedCookiesJson)

    storedCookies?.let { retrievedCookies ->
      cookies.addAll(retrievedCookies)
    }

    return cookies
  }

  private fun toJson(cookies: Set<String>): String =
    jsonAdapter.toJson(cookies)

  private fun fromJson(cookiesJson: String): Set<String>? =
    jsonAdapter.fromJson(cookiesJson)
}
