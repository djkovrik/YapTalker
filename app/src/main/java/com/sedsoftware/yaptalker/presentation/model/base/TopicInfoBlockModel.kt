package com.sedsoftware.yaptalker.presentation.model.base

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
)
