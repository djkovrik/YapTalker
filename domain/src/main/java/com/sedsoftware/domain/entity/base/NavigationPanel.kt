package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.YapEntityTypes

/**
 * Class which represents navigation panel in domain layer.
 */
class NavigationPanel(
    val currentPage: Int,
    val totalPages: Int
) : YapEntity {

  override fun getEntityType(): Int = YapEntityTypes.NAVIGATION_PANEL
}
