package com.sedsoftware.yaptalker.model.base

import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.YapEntityTypes

/**
 * Class which represents navigation panel in domain layer.
 */
class NavigationPanelModel(
    val currentPage: Int,
    val totalPages: Int
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.NAVIGATION_PANEL_ITEM
}
