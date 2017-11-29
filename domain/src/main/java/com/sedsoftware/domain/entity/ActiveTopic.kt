package com.sedsoftware.domain.entity

/**
 * Class which represents active topic item in domain layer.
 */
class ActiveTopic(
    val title: String,
    val link: String,
    val isPinned: String,
    val isClosed: String,
    val forumTitle: String,
    val forumLink: String,
    val rating: String,
    val answers: String,
    val lastPostDate: String
)
