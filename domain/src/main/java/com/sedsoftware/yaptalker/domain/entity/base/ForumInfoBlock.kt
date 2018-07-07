package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents forum info block in domain layer.
 */
class ForumInfoBlock(
    val forumTitle: String,
    val forumId: Int
) : BaseEntity
