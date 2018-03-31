package com.sedsoftware.yaptalker.data.parsed

import com.squareup.moshi.Json

class AppVersionInfo(
  @Json(name = "version_code") val versionCode: Int,
  @Json(name = "version_name") val versionName: String,
  @Json(name = "link") val downloadLink: String
)
