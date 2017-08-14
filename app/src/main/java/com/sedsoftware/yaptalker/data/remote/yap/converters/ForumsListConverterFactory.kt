package com.sedsoftware.yaptalker.data.remote.yap.converters

import com.sedsoftware.yaptalker.data.model.ForumItem
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ForumsListConverterFactory : Converter.Factory() {

  companion object {
    fun create() = ForumsListConverterFactory()
  }

  override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?,
      retrofit: Retrofit?): Converter<ResponseBody, List<ForumItem>>? {

    return ForumsListResponseBodyConverter()
  }
}