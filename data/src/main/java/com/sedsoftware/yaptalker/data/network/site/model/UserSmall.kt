package com.sedsoftware.yaptalker.data.network.site.model

import com.google.gson.annotations.SerializedName

data class UserSmall(
    @SerializedName("avatar_res")
    var avatarRes: String? = null,
    @SerializedName("avatar_type")
    var avatarType: String? = null,
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("new_mails")
    var newMails: String? = null,
    @SerializedName("rank")
    var rank: Int? = null,
    @SerializedName("read_only")
    var readOnly: Int? = null,
    @SerializedName("SID")
    var sid: String? = null,
    @SerializedName("validated")
    var validated: String? = null
)
