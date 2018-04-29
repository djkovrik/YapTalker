package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

class SearchTopicsPageInfoModel(
  val hasNextPage: Boolean,
  val searchId: String
) : DisplayedItemModel {

  override fun getEntityType(): Int = DisplayedItemType.SEARCH_TOPIC_INFO
}

