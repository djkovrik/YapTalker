package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

/**
 * Class which represents topic info block in presentation layer.
 */
class TopicInfoBlockModel(
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

  override fun getBaseEntityType(): Int = ItemType.TOPIC_INFO
}
