package com.sedsoftware.yaptalker.data.model

data class PostItem(val id: Int, val author: UserProfile, val date: String, val uq: Int,
    val content: String)