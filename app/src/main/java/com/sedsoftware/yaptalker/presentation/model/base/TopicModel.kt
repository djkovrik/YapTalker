package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

class TopicModel(
  val title: String,
  val link: String,
  val id: Int,
  val isPinned: Boolean,
  val isClosed: Boolean,
  val author: String,
  val authorLink: String,
  val rating: Int,
  val ratingText: String,
  val answers: String,
  val lastPostDate: String,
  val lastPostAuthor: String
) : DisplayedItemModel {

  override fun getEntityType(): Int = DisplayedItemType.TOPIC
}
