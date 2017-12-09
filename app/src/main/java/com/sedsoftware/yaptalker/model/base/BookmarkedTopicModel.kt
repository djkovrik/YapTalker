package com.sedsoftware.yaptalker.model.base

import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.YapEntityTypes

/**
 * Class which represents bookmarked topic item in presentation layer.
 */
class BookmarkedTopicModel(
    val bookmarkId: Int,
    val title: String,
    val link: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.BOOKMARKED_TOPIC_ITEM
}
