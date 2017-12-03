package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.BaseEntityTypes

/**
 * Class which represents news item in domain layer.
 */
class NewsItem(
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
) : BaseEntity {

  override fun getBaseEntityType(): Int = BaseEntityTypes.NEWS
}
