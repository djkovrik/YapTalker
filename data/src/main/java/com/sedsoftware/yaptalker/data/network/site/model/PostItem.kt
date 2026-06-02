package com.sedsoftware.yaptalker.data.network.site.model

import com.google.gson.annotations.SerializedName

data class PostItem(
    @SerializedName("cat_id")
    var catId: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("title")
    var title: String? = null
)
