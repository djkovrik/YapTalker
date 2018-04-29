package com.sedsoftware.yaptalker.common.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class HashSearchConverterFactory(private val marker: String) : Converter.Factory() {

  companion object {
    fun create(hashMarker: String) = HashSearchConverterFactory(hashMarker)
  }

  override fun responseBodyConverter(
    type: Type?,
    annotations: Array<out Annotation>?,
    retrofit: Retrofit?
  ): Converter<ResponseBody, String>? = HashSearchResponseBodyConverter(marker)
}
