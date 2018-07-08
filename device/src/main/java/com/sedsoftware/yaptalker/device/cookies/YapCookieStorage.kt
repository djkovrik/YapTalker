package com.sedsoftware.yaptalker.device.cookies

import com.sedsoftware.yaptalker.domain.device.CookieStorage
import com.sedsoftware.yaptalker.domain.device.Settings
import javax.inject.Inject

class YapCookieStorage @Inject constructor(
    private val settings: Settings
) : CookieStorage {

    companion object {
        private const val SID_COOKIE_MARKER = "SID"
        private const val DELETED_MARKER = "deleted"
    }

    override fun saveCookie(cookie: String) {
        if (cookie.contains(SID_COOKIE_MARKER)) {
            val sid = cookie.substringAfter("=").substringBefore(";")
            if (sid != DELETED_MARKER) {
                settings.saveSingleCookie(sid)
            }
        }
    }

    override fun getCookie(): String {
        val sid = settings.getSingleCookie()
        return if (sid.isNotEmpty()) {
            "$SID_COOKIE_MARKER=$sid"
        } else {
            ""
        }
    }
}
