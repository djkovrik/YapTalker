package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

/**
 * Class which represents active topic item in presentation layer.
 */
class ActiveTopicModel(
    val title: String,
    val link: String,
    val topicId: Int,
    val isPinned: Boolean,
    val isClosed: Boolean,
    val forumTitle: String,
    val forumLink: String,
    val forumId: Int,
    val rating: Int,
    val ratingText: String,
    val answers: String,
    val lastPostDate: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.ACTIVE_TOPIC_ITEM
}
