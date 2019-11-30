package com.sedsoftware.yaptalker.domain.device

@Suppress("ComplexInterface", "TooManyFunctions")
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
    fun getDrawerItems(): Set<String>
    fun getLastUpdateCheckDate(): Long
    fun getCurrentTheme(): String
    fun isNsfwEnabled(): Boolean
    fun isExternalCoubPlayer(): Boolean
    fun isExternalYapPlayer(): Boolean
    fun isScreenAlwaysOnEnabled(): Boolean
    fun isHttpsEnabled(): Boolean
    fun isInTwoPaneMode(): Boolean
    fun isVideoLandscapeEnabled(): Boolean
    fun saveSingleCookie(cookie: String)
    fun saveMessagesPerPagePref(value: Int)
    fun saveTopicsPerPagePref(value: Int)
    fun saveLastUpdateCheckDate()
    fun saveLogin(login: String)
    fun savePassword(password: String)
    fun getLogin(): String
    fun getPassword(): String
}
