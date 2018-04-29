package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

class NavigationPanelModel(
  val currentPage: Int,
  val totalPages: Int,
  val navigationLabel: String
) : DisplayedItemModel {

  override fun getEntityType(): Int = DisplayedItemType.NAVIGATION_PANEL
}
