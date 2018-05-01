package com.sedsoftware.yaptalker.domain.entity.base

import java.util.Date

class BlacklistedTopic(
  val topicName: String,
  val topicLink: String,
  val topicId: Int,
  val dateAdded: Date
)
