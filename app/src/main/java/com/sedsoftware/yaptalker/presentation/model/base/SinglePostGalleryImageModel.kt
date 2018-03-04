package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

class SinglePostGalleryImageModel(
  val url: String,
  val showLoadMore: Boolean
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.TOPIC_PAGE_IMAGES
}
