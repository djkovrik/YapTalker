package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

class ForumInfoBlockModel(
  val forumTitle: String,
  val forumId: Int
) : DisplayedItemModel {

  override fun getEntityType(): Int = DisplayedItemType.FORUM_INFO
}

