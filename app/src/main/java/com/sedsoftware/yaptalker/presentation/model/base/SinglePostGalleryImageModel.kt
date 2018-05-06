package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

class SinglePostGalleryImageModel(
  val url: String,
  val showLoadMore: Boolean
) : DisplayedItemModel {

  override fun getEntityType(): Int = DisplayedItemType.GALLERY_IMAGE
}
