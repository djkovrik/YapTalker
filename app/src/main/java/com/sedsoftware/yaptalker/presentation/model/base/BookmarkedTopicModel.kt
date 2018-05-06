package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

class BookmarkedTopicModel(
  val bookmarkId: Int,
  val title: String,
  val link: String
) : DisplayedItemModel {

  override fun getEntityType(): Int = DisplayedItemType.BOOKMARKED_TOPIC
}
