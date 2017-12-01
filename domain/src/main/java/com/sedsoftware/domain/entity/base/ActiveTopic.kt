package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.YapEntityTypes

/**
 * Class which represents active topic item in domain layer.
 */
class ActiveTopic(
    val title: String,
    val link: String,
    val isPinned: Boolean,
    val isClosed: Boolean,
    val forumTitle: String,
    val forumLink: String,
    val rating: Int,
    val answers: Int,
    val lastPostDate: String
) : YapEntity {

  override fun getEntityType(): Int = YapEntityTypes.ACTIVE_TOPIC_ITEM
}
