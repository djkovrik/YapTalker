package com.sedsoftware.domain.entity

/**
 * Class which represents news item in domain layer.
 */
class NewsItem(
    val title: String,
    val link: String,
    val rating: String,
    val description: String,
    val images: List<String>,
    val videos: List<String>,
    val videosRaw: List<String>,
    val author: String,
    val authorLink: String,
    val date: String,
    val forumName: String,
    val forumLink: String,
    val comments: String,
    val cleanedDescription: String
)
