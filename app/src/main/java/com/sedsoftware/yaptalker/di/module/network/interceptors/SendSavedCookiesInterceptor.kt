package com.sedsoftware.yaptalker.di.module.network.interceptors

import com.sedsoftware.yaptalker.domain.device.CookieStorage
import okhttp3.Interceptor
import okhttp3.Response

class SendSavedCookiesInterceptor(private val cookieStorage: CookieStorage) : Interceptor {

    companion object {
        const val HEADER_SKIP_SAVED_COOKIES = "Skip-Saved-Cookies"

        private const val API_LOGIN_PATH = "/action/login"
        private const val COOKIE_HEADER = "Cookie"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val skipSavedCookies = chain.request().header(HEADER_SKIP_SAVED_COOKIES) != null
        val builder = chain.request()
            .newBuilder()
            .removeHeader(HEADER_SKIP_SAVED_COOKIES)
        val sidCookie = cookieStorage.getCookie()

        if (skipSavedCookies) {
            if (chain.request().url().encodedPath().contains(API_LOGIN_PATH)) {
                builder.removeHeader(COOKIE_HEADER)
            }
        } else if (sidCookie.isNotEmpty() && chain.request().header(COOKIE_HEADER).isNullOrEmpty()) {
            builder.header(COOKIE_HEADER, sidCookie)
        }

        return chain.proceed(builder.build())
    }
}
