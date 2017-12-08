package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity

/**
 * Class which represents bookmarked topic item in domain layer.
 */
class BookmarkedTopic(
    val bookmarkId: Int,
    val title: String,
    val link: String
) : BaseEntity
