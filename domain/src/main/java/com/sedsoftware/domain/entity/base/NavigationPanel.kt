package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity

/**
 * Class which represents navigation panel in domain layer.
 */
class NavigationPanel(
    val currentPage: Int,
    val totalPages: Int
) : BaseEntity
