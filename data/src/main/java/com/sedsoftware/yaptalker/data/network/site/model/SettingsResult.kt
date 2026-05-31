package com.sedsoftware.yaptalker.data.network.site.model

import com.google.gson.annotations.SerializedName

data class SettingsResult(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("global")
    var global: GlobalParam? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("user")
    var user: UserSmall? = null
)
