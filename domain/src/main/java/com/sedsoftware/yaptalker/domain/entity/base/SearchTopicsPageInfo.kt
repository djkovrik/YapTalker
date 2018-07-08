package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents search results topic page info in domain layer.
 */
class SearchTopicsPageInfo(
    val hasNextPage: Boolean,
    val searchId: String
) : BaseEntity
