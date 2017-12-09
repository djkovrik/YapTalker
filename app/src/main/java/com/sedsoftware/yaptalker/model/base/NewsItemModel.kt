package com.sedsoftware.yaptalker.model.base

import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.YapEntityTypes

/**
 * Class which represents news item in domain layer.
 */
class NewsItemModel(
    val title: String,
    val link: String,
    val rating: Int,
    val description: String,
    val images: List<String>,
    val videos: List<String>,
    val videosRaw: List<String>,
    val author: String,
    val authorLink: String,
    val date: String,
    val forumName: String,
    val forumLink: String,
    val comments: Int,
    val cleanedDescription: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.NEWS_ITEM
}
