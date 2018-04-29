package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

class SinglePostGalleryImageModel(
  val url: String,
  val showLoadMore: Boolean
) : YapEntity {

  override fun getBaseEntityType(): Int = ItemType.TOPIC_PAGE_IMAGES
}
