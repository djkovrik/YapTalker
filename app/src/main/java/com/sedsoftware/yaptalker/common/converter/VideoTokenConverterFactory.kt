package com.sedsoftware.yaptalker.common.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class VideoTokenConverterFactory(
    private val startMarker: String,
    private val endMarker: String
) : Converter.Factory() {

    companion object {
        fun create(startMarker: String, endMarker: String) = VideoTokenConverterFactory(startMarker, endMarker)
    }

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<out Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, String>? = VideoTokenResponseBodyConverter(startMarker, endMarker)
}
