package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents topic item in domain layer.
 */
class Topic(
    val title: String,
    val link: String,
    val id: Int,
    val isPinned: Boolean,
    val isClosed: Boolean,
    val author: String,
    val authorLink: String,
    val rating: Int,
    val answers: Int,
    val lastPostDate: String,
    val lastPostAuthor: String
) : BaseEntity
