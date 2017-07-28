package com.sedsoftware.yaptalker.data

// NEWS
data class NewsItem(val summary: String, val topic: TopicItemList)

// CHOSEN FORUM
data class TopicItemList(val id: Int, val title: String, val answers: Int, val uq: Int,
    val author: UserProfileShort, val date: String)

data class UserProfileShort(val id: Int, val name: String)

// FORUMS LIST
data class ForumItem(val id: Int, val title: String, val lastTopic: TopicItemShort)

data class TopicItemShort(val id: Int, val title: String, val author: UserProfileShort,
    val date: String)

// CHOSEN TOPIC
data class TopicItemFull(val title: String, val uq: Int, val posts: List<PostItem>)

data class PostItem(val id: Int, val author: UserProfile, val date: String, val uq: Int,
    val content: String)

data class UserProfile(val id: Int, val name: String, val avatar: String, val registered: String,
    val messagesCount: Int)
