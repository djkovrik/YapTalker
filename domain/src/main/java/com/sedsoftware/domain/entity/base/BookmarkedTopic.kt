package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.BaseEntityTypes

/**
 * Class which represents bookmarked topic item in domain layer.
 */
class BookmarkedTopic(
    val bookmarkId: Int,
    val title: String,
    val link: String
) : BaseEntity {

  override fun getBaseEntityType(): Int = BaseEntityTypes.BOOKMARKED_TOPIC
}
