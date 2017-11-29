package com.sedsoftware.domain.entity

/**
 * Class which represents topic item in domain layer.
 */
class Topic(
    val title: String,
    val link: String,
    val isPinned: String,
    val isClosed: String,
    val author: String,
    val authorLink: String,
    val rating: String,
    val answers: String,
    val lastPostDate: String,
    val lastPostAuthor: String
)
