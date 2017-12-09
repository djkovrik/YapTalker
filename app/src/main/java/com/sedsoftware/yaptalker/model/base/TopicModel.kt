package com.sedsoftware.yaptalker.model.base

import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.YapEntityTypes

/**
 * Class which represents topic item in presentation layer.
 */
class TopicModel(
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
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.TOPIC_ITEM
}
