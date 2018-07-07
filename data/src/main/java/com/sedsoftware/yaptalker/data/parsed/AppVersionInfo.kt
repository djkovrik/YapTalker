package com.sedsoftware.yaptalker.data.parsed

import com.google.gson.annotations.SerializedName

class AppVersionInfo(
    @SerializedName(value = "version_code")
    val versionCode: Int,
    @SerializedName(value = "version_name")
    val versionName: String,
    @SerializedName(value = "link")
    val downloadLink: String
)
