package com.sedsoftware.yaptalker.presentation.model.base

import android.text.Spanned
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

/**
 * Class which represents news item in domain layer.
 */
class NewsItemModel(
    val title: String,
    val link: String,
    val topicId: Int,
    val rating: Spanned,
    val images: List<String>,
    val videos: List<String>,
    val videosRaw: List<String>,
    val author: String,
    val authorLink: String,
    val date: String,
    val forumName: String,
    val forumLink: String,
    val forumId: Int,
    val comments: String,
    val cleanedDescription: Spanned,
    val isYapLink: Boolean
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.NEWS_ITEM
}
