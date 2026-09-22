package com.sedsoftware.yaptalker.data.network.site.model

import com.google.gson.annotations.SerializedName

data class UserProfileApi(
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,
    @SerializedName("birthday")
    var birthday: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("group_title")
    var groupTitle: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("joined")
    var joined: String? = null,
    @SerializedName("location")
    var location: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("photo_url")
    var photoUrl: String? = null,
    @SerializedName("posts")
    var posts: String? = null,
    @SerializedName("rank_value")
    var rankValue: String? = null,
    @SerializedName("sex")
    var sex: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("time_zone")
    var timeZone: String? = null
)
