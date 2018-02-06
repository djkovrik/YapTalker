package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

class SearchTopicItemModel(
  val title: String,
  val link: String,
  val topicId: Int,
  val isPinned: Boolean,
  val isClosed: Boolean,
  val forumTitle: String,
  val forumLink: String,
  val forumId: Int,
  val rating: Int,
  val answers: Int,
  val lastPostDate: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.SEARCH_TOPIC_ITEM
}
