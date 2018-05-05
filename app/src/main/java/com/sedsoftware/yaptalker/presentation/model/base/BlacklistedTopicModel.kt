package com.sedsoftware.yaptalker.presentation.model.base

import java.util.Date

class BlacklistedTopicModel(
  val topicName: String,
  val topicLink: String,
  val topicId: Int,
  val dateAdded: Date,
  val dateAddedLabel: String
)
