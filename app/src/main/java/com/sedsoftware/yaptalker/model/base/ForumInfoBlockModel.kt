package com.sedsoftware.yaptalker.model.base

import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.YapEntityTypes

/**
 * Class which represents forum info block in presentation layer.
 */
class ForumInfoBlockModel(
    val forumTitle: String,
    val forumId: Int
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.FORUM_INFO_BLOCK
}
