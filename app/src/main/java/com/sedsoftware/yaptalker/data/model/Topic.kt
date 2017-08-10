package com.sedsoftware.yaptalker.data.model

data class TopicItemList(val id: Int, val title: String, val answers: Int, val uq: Int,
    val author: UserProfileShort, val date: String)

data class TopicItemShort(val id: Int, val title: String, val author: UserProfileShort,
    val date: String)
