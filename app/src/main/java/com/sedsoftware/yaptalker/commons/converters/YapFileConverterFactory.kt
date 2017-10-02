package com.sedsoftware.yaptalker.commons.converters

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class YapFileConverterFactory : Converter.Factory() {

  companion object {
    fun create() = YapFileConverterFactory()
  }

  override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?,
      retrofit: Retrofit?): Converter<ResponseBody, String>? {

    return YapFileResponseBodyConverter()
  }
}
