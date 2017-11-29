package com.sedsoftware.domain.entity

/**
 * Class which represents single topic post item in domain layer.
 */
class SinglePost(
    val authorNickname: String,
    val authorProfile: String,
    val authorAvatar: String,
    val authorMessagesCount: String,
    val postDate: String,
    val postRank: String,
    val postRankPlusAvailable: String,
    val postRankMinusAvailable: String,
    val postRankPlusClicked: String,
    val postRankMinusClicked: String,
    val postContent: SinglePostParsed,
    val postId: String
)
