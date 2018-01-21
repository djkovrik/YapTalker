package com.sedsoftware.yaptalker.di.modules.network.cookies

import com.sedsoftware.yaptalker.domain.device.Settings

class CustomCookieStorage(private val settings: Settings) {

  fun saveToPrefs(sid: String) {
    settings.saveCookieSid(sid)
  }

  fun getFromPrefs(): String = settings.getCookieSid()
}
