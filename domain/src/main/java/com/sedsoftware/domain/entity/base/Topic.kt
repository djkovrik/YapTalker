package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.BaseEntityTypes

/**
 * Class which represents topic item in domain layer.
 */
class Topic(
    val title: String,
    val link: String,
    val isPinned: Boolean,
    val isClosed: Boolean,
    val author: String,
    val authorLink: String,
    val rating: Int,
    val answers: Int,
    val lastPostDate: String,
    val lastPostAuthor: String
) : BaseEntity {

  override fun getBaseEntityType(): Int = BaseEntityTypes.TOPIC
}
