package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.BaseEntityTypes

class ForumInfoBlock(
    val forumTitle: String,
    val forumId: Int
) : BaseEntity {

  override fun getBaseEntityType(): Int = BaseEntityTypes.FORUM_INFO
}
