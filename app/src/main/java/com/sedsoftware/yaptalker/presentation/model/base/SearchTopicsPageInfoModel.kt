package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

class SearchTopicsPageInfoModel(
  val hasNextPage: Boolean,
  val searchId: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.SEARCH_PAGE_INFO
}
