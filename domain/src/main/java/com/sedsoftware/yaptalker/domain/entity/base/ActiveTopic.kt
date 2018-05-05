package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents active topic item in domain layer.
 */
class ActiveTopic(
  val title: String,
  val link: String,
  val id: Int,
  val isPinned: Boolean,
  val isClosed: Boolean,
  val forumTitle: String,
  val forumLink: String,
  val rating: Int,
  val answers: Int,
  val lastPostDate: String
) : BaseEntity
