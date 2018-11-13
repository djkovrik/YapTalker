package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

data class TopicInfoBlockModel(
    val topicTitle: String,
    val isClosed: Boolean,
    val authKey: String,
    val topicRating: Int,
    val topicRatingPlusAvailable: Boolean,
    val topicRatingMinusAvailable: Boolean,
    val topicRatingPlusClicked: Boolean,
    val topicRatingMinusClicked: Boolean,
    val topicRatingTargetId: Int
) : DisplayedItemModel {

    override fun getEntityType(): Int = DisplayedItemType.TOPIC_INFO
}
