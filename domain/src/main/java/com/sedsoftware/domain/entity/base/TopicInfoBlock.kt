package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.YapEntityTypes

class TopicInfoBlock(
    val topicTitle: String,
    val isClosed: Boolean,
    val authKey: String,
    val topicRating: Int,
    val topicRatingPlusAvailable: Boolean,
    val topicRatingMinusAvailable: Boolean,
    val topicRatingPlusClicked: Boolean,
    val topicRatingMinusClicked: Boolean,
    val topicRatingTargetId: Int
) : YapEntity {

  override fun getEntityType(): Int = YapEntityTypes.TOPIC_INFO_BLOCK
}
