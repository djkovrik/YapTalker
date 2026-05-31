package com.sedsoftware.yaptalker.data.network.site.model

import com.google.gson.annotations.SerializedName

data class VoteResult(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("global")
    var global: GlobalParam? = null,
    @SerializedName("post_rank")
    var postRank: Int? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("user")
    var user: UserSmall? = null
)
