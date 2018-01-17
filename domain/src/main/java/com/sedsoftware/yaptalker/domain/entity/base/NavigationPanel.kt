package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents navigation panel in domain layer.
 */
class NavigationPanel(
  val currentPage: Int,
  val totalPages: Int
) : BaseEntity
