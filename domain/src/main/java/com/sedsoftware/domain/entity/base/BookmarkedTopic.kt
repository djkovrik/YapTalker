package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.YapEntityTypes

/**
 * Class which represents bookmarked topic item in domain layer.
 */
class BookmarkedTopic(
    val bookmarkId: Int,
    val title: String,
    val link: String
) : YapEntity {

  override fun getEntityType(): Int = YapEntityTypes.BOOKMARKED_TOPIC_ITEM
}
