package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

class SearchTopicsPageInfoModel(
  val hasNextPage: Boolean,
  val searchId: String
) : YapEntity {

  override fun getBaseEntityType(): Int = ItemType.SEARCH_PAGE_INFO
}
