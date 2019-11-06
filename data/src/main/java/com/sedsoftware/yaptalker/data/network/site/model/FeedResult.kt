package com.sedsoftware.yaptalker.data.network.site.model

import com.google.gson.annotations.SerializedName

data class FeedResult(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("global")
    var global: GlobalParam? = null,
    @SerializedName("offset")
    var offset: String? = null,
    @SerializedName("user")
    var user: UserSmall? = null
)
