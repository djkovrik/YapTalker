package com.sedsoftware.yaptalker.domain.device

interface Settings {

  fun saveCookies(cookies: String)

  fun getCookies() : String

  fun clearCookies()

  fun getStartingPage(): String

  fun getAvatarSize(): Int

  fun getNormalFontSize(): Float

  fun getBigFontSize(): Float

  fun getSmallFontSize(): Float

  fun getMessagesPerPage(): Int

  fun getTopicsPerPage(): Int

  fun saveMessagesPerPagePref(value: Int)

  fun saveTopicsPerPagePref(value: Int)

  fun getNewsCategories(): Set<String>

  fun isNsfwEnabled(): Boolean

  fun isScreenAlwaysOnEnabled(): Boolean

  fun isHttpsEnabled(): Boolean
}
