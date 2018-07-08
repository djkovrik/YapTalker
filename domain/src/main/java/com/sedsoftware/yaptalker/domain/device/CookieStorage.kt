package com.sedsoftware.yaptalker.domain.device

interface CookieStorage {

    fun saveCookie(cookie: String)

    fun getCookie(): String
}
