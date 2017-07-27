package com.sedsoftware.yaptalker.data

data class NewsItem(val summary: String, val topic: TopicItem)

data class ForumItem(val id: Int, val title: String, val lastTopic: TopicItemShort)

data class TopicItemShort(val id: Int, val title: String, val author: UserProfileShort,
    val date: String)

data class TopicItem(val id: Int, val title: String, val answers: Int, val views: Int, val uq: Int,
    val author: UserProfileShort, val date: String)

data class PostItem(val id: Int, val author: UserProfileShort, val date: String, val uq: String,
    val content: String)

data class UserProfileShort(val id: Int, val name: String)

data class UserProfileInfo(val shortInfo: UserProfileShort, val picture: String, val avatar: String,
    val messagesCount: Int, val uq: Int)