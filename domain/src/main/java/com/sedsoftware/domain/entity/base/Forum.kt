package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.BaseEntityTypes

/**
 * Class which represents forum item in domain layer.
 */
class Forum(
    val title: String,
    val forumId: Int,
    val lastTopicTitle: String,
    val lastTopicAuthor: String,
    val date: String
) : BaseEntity {

  override fun getBaseEntityType(): Int = BaseEntityTypes.FORUM
}
