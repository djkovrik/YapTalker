package com.sedsoftware.yaptalker.data.remote.converters

import com.sedsoftware.yaptalker.data.NewsItem
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class NewsConverterFactory : Converter.Factory() {

  override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?,
      retrofit: Retrofit?): Converter<ResponseBody, List<NewsItem>>? {

    return NewsResponseBodyConverter()
  }

  override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?,
      methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {

    return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
  }

  override fun stringConverter(type: Type?, annotations: Array<out Annotation>?,
      retrofit: Retrofit?): Converter<*, String>? {

    return super.stringConverter(type, annotations, retrofit)
  }
}