package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

/**
 * Class which represents forum info block in presentation layer.
 */
class ForumInfoBlockModel(
  val forumTitle: String,
  val forumId: Int
) : YapEntity {

  override fun getBaseEntityType(): Int = ItemType.FORUM_INFO
}
