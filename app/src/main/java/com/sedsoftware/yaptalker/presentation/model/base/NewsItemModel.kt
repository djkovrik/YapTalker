package com.sedsoftware.yaptalker.presentation.model.base

import android.text.Spanned
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

data class NewsItemModel(
    val title: String,
    val link: String,
    val topicId: Int,
    val rating: Spanned,
    val images: List<String>,
    val videos: List<String>,
    var videosRaw: List<String>,
    var videosLinks: List<String>,
    var videoTypes: List<String>,
    val author: String,
    val authorLink: String,
    val date: String,
    val forumName: String,
    val forumLink: String,
    val forumId: Int,
    val comments: String,
    val cleanedDescription: Spanned,
    val isYapLink: Boolean
) : DisplayedItemModel {

    override fun getEntityType(): Int = DisplayedItemType.NEWS
}
