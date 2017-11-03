package com.sedsoftware.yaptalker.commons.converters

import okhttp3.ResponseBody
import retrofit2.Converter

class HashSearchResponseBodyConverter(private val marker: String) : Converter<ResponseBody, String> {

  companion object {
    private const val HASH_LENGTH = 32
  }

  override fun convert(value: ResponseBody): String {
    return value.string().substringAfter(marker).take(HASH_LENGTH)
  }
}
