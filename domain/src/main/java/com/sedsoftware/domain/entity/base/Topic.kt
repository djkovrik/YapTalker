package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.YapEntityTypes

/**
 * Class which represents topic item in domain layer.
 */
class Topic(
    val title: String,
    val link: String,
    val isPinned: Boolean,
    val isClosed: Boolean,
    val author: String,
    val authorLink: String,
    val rating: Int,
    val answers: Int,
    val lastPostDate: String,
    val lastPostAuthor: String
) : YapEntity {

  override fun getEntityType(): Int = YapEntityTypes.TOPIC_ITEM
}
