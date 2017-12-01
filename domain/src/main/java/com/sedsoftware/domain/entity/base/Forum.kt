package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.YapEntityTypes

/**
 * Class which represents forum item in domain layer.
 */
class Forum(
    val title: String,
    val forumId: Int,
    val lastTopicTitle: String,
    val lastTopicAuthor: String,
    val date: String
) : YapEntity {

  override fun getEntityType(): Int = YapEntityTypes.FORUM_ITEM
}
