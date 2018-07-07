package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents emoji item in domain layer.
 */
class Emoji(
    val code: String,
    val link: String
) : BaseEntity
