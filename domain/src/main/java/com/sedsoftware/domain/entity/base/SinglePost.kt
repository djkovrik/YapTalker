package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity

/**
 * Class which represents single topic post item in domain layer.
 */
class SinglePost(
    val authorNickname: String,
    val authorProfile: String,
    val authorAvatar: String,
    val authorMessagesCount: Int,
    val postDate: String,
    val postRank: String,
    val postRankPlusAvailable: Boolean,
    val postRankMinusAvailable: Boolean,
    val postRankPlusClicked: Boolean,
    val postRankMinusClicked: Boolean,
    val postContentParsed: SinglePostParsed,
    val postId: Int
) : BaseEntity
