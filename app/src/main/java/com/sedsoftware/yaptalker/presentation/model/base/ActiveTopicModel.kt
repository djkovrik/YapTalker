package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

data class ActiveTopicModel(
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
) : DisplayedItemModel {

    override fun getEntityType(): Int = DisplayedItemType.ACTIVE_TOPIC
}
