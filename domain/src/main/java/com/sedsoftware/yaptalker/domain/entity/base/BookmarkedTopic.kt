package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents bookmarked topic item in domain layer.
 */
class BookmarkedTopic(
  val bookmarkId: Int,
  val title: String,
  val link: String
) : BaseEntity
