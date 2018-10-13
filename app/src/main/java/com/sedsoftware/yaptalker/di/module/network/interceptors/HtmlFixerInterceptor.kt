package com.sedsoftware.yaptalker.di.module.network.interceptors

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import okhttp3.ResponseBody

class HtmlFixerInterceptor : Interceptor {

    companion object {
        private const val SEARCH_REGEX = "<div align=.* id=[\"|'](.*)[\"|'] rel=[\"|']yapfiles[\"|'].*/>"

        private const val REPLACING_TEXT =
            """
                <div align="center" id="$1" rel="yapfiles">
                    <iframe src="//www.yapfiles.ru/get_player/?v=$1" width="640" height="360"
                        frameborder="0" webkitallowfullscreen="" mozallowfullscreen="" allowfullscreen="">
                    </iframe>
                </div>
            """
    }

    override fun intercept(chain: Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val originalBodyString = originalResponse.body()?.string()
        val originalContentType = originalResponse.body()?.contentType()
        val newBodyString = originalBodyString?.fixYapHtml() ?: ""
        return originalResponse.newBuilder().body(ResponseBody.create(originalContentType, newBodyString)).build()
    }

    private fun String?.fixYapHtml(): String = this?.replace(Regex(SEARCH_REGEX), REPLACING_TEXT) ?: ""
}
