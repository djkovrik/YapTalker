package com.sedsoftware.yaptalker.di.module.network.interceptors

import com.sedsoftware.yaptalker.domain.device.CookieStorage
import okhttp3.Interceptor
import okhttp3.Response

class SaveReceivedCookiesInterceptor(private val cookieStorage: CookieStorage) : Interceptor {

    companion object {
        private const val COOKIE_HEADER_MARKER = "Set-Cookie"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val cookies = originalResponse.headers(COOKIE_HEADER_MARKER)
        cookies.forEach { cookie -> cookieStorage.saveCookie(cookie) }
        return originalResponse
    }
}
