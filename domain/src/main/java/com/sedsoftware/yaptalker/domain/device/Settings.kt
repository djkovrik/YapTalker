package com.sedsoftware.yaptalker.domain.device

interface Settings {

  fun getSingleCookie(): String

  fun getStartingPage(): String

  fun getAvatarSize(): Int

  fun getNormalFontSize(): Float

  fun getBigFontSize(): Float

  fun getSmallFontSize(): Float

  fun getMessagesPerPage(): Int

  fun getTopicsPerPage(): Int

  fun getNewsCategories(): Set<String>

  fun getLastUpdateCheckDate(): Long

  fun getCurrentTheme(): String

  fun isNsfwEnabled(): Boolean

  fun isExternalCoubPlayer(): Boolean

  fun isScreenAlwaysOnEnabled(): Boolean

  fun isHttpsEnabled(): Boolean

  fun isInTwoPaneMode(): Boolean

  fun saveSingleCookie(cookie: String)

  fun saveMessagesPerPagePref(value: Int)

  fun saveTopicsPerPagePref(value: Int)

  fun saveLastUpdateCheckDate()
}
