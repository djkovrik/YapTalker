package com.sedsoftware.domain.entity

/**
 * Class which represents forum item in domain layer.
 */
class Forum(
    val title: String,
    val forumId: Int,
    val lastTopicTitle: String,
    val lastTopicAuthor: String,
    val date: String
)
