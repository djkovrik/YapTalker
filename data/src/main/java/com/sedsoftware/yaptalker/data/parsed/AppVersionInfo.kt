package com.sedsoftware.yaptalker.data.parsed

import com.google.gson.annotations.SerializedName

class AppVersionInfo(
  @SerializedName("version_code") val versionCode: Int,
  @SerializedName("version_name") val versionName: String,
  @SerializedName("link") val downloadLink: String
)
