package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

/**
 * Class which represents bookmarked topic item in presentation layer.
 */
class BookmarkedTopicModel(
  val bookmarkId: Int,
  val title: String,
  val link: String
) : YapEntity {

  override fun getBaseEntityType(): Int = ItemType.BOOKMARKED_TOPIC
}
