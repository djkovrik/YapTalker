package com.sedsoftware.yaptalker.common.converter

import okhttp3.ResponseBody
import retrofit2.Converter

class HashSearchResponseBodyConverter(private val marker: String) : Converter<ResponseBody, String> {

    companion object {
        private const val HASH_LENGTH = 32
    }

    override fun convert(value: ResponseBody): String = value.string().substringAfter(marker).take(HASH_LENGTH)
}
