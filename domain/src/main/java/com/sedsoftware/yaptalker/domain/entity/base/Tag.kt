package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents single topic post tag item in domain layer.
 */
class Tag(
    val name: String,
    val link: String,
    val searchParameter: String
) : BaseEntity
