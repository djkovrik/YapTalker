package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

/**
 * Class which represents topic item in presentation layer.
 */
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
) : YapEntity {

  override fun getBaseEntityType(): Int = ItemType.TOPIC
}
