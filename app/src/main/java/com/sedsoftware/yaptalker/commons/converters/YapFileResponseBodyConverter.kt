package com.sedsoftware.yaptalker.commons.converters

import okhttp3.ResponseBody
import retrofit2.Converter

class YapFileResponseBodyConverter : Converter<ResponseBody, String> {

  companion object {
    private const val HASH_MARKER = "md5="
    private const val HASH_LENGTH = 32
  }

  override fun convert(value: ResponseBody): String {
    return value.string().substringAfter(HASH_MARKER).take(HASH_LENGTH)
  }
}
