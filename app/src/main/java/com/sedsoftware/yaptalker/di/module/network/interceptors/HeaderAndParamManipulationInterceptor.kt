package com.sedsoftware.yaptalker.di.module.network.interceptors

import android.os.Build
import android.os.Build.VERSION
import com.sedsoftware.yaptalker.YapTalkerApp
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class HeaderAndParamManipulationInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        var request: Request
        val request2 = chain.request()
        val newBuilder = request2.url().newBuilder()
        val str = "md5"
        if (request2.url().queryParameter(str) == null) {
            newBuilder.addQueryParameter(str, YapTalkerApp.getMd5())
        }
        val str2 = "appVersion"
        if (request2.url().queryParameter(str2) == null) {
            newBuilder.addQueryParameter(str2, YapTalkerApp.getAppVersion())
        }
        val str3 = "type"
        if (request2.url().queryParameter(str3) == null) {
            newBuilder.addQueryParameter(str3, "json")
        }
        val str4 = "Connection"
        if (request2.header(str4) == null) {
            request = request2.newBuilder().addHeader(str4, "keep-alive").url(newBuilder.build()).build()
        } else {
            request = request2.newBuilder().url(newBuilder.build()).build()
        }
        val str5 = "User-Agent"
        if (request.header(str5) == null) {
            val sb = StringBuilder()
            sb.append("Yaplakal/0.998 (Android ")
            sb.append(VERSION.RELEASE)
            sb.append("; ")
            sb.append(Build.MANUFACTURER)
            val str6 = ","
            sb.append(str6)
            sb.append(Build.MODEL)
            sb.append(str6)
            sb.append(VERSION.SDK_INT)
            sb.append(")")
            request = request.newBuilder().addHeader(str5, sb.toString()).url(newBuilder.build()).build()
        }
        val str7 = "http-udid"
        return if (request.header(str7) != null) {
            chain.proceed(request)
        } else chain.proceed(request.newBuilder().header(str7, YapTalkerApp.getUdid()).build())
    }
}
