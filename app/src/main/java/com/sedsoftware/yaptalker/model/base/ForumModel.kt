package com.sedsoftware.yaptalker.model.base

import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.YapEntityTypes

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
