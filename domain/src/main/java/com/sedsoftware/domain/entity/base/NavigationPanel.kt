package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.BaseEntityTypes

/**
 * Class which represents navigation panel in domain layer.
 */
class NavigationPanel(
    val currentPage: Int,
    val totalPages: Int
) : BaseEntity {

  override fun getBaseEntityType(): Int = BaseEntityTypes.NAVIGATION_PANEL
}
