package com.sedsoftware.yaptalker.data.network.site.model

import com.google.gson.annotations.SerializedName

data class GlobalParam(
    @SerializedName("api_version")
    var apiVersion: String? = null,
    @SerializedName("app_version")
    var appVersion: String? = null,
    @SerializedName("generation_time")
    var generationTime: Double? = null,
    @SerializedName("server_time")
    var serverTime: String? = null
)
