package com.sedsoftware.yaptalker.common.converter

import okhttp3.ResponseBody
import retrofit2.Converter

class VideoTokenResponseBodyConverter(
    private val startMarker: String,
    private val endMarker: String
) : Converter<ResponseBody, String> {

    override fun convert(value: ResponseBody): String =
        value.string().substringAfter(startMarker).substringBefore(endMarker)
}
