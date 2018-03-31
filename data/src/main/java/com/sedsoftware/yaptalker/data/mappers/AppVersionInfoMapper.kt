package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.VersionInfo
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AppVersionInfoMapper @Inject constructor() : Function<Response<ResponseBody>, BaseEntity> {

  override fun apply(from: Response<ResponseBody>): BaseEntity {
    // TODO Replace with moshi json parsing
    val responseText = from.body()?.string() ?: ""
    return VersionInfo(
      versionCode = responseText
        .substringAfter("version_code\": ")
        .substringBefore(",")
        .toInt(),
      versionName = responseText
        .substringAfter("version_name\": \"")
        .substringBefore("\""),
      downloadLink = responseText
        .substringAfter("link\": ")
        .substringBefore("\"")
      )
  }
}
