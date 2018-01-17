package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

/**
 * Class which represents forum item in presentation layer.
 */
class ForumModel(
  val title: String,
  val forumId: Int,
  val lastTopicTitle: String,
  val lastTopicAuthor: String,
  val date: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.FORUM_ITEM
}
