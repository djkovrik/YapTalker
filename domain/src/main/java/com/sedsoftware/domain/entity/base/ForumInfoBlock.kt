package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.YapEntityTypes

class ForumInfoBlock(
    val forumTitle: String,
    val forumId: Int
) : YapEntity {

  override fun getEntityType(): Int = YapEntityTypes.FORUM_INFO_BLOCK
}
