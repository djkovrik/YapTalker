package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

/**
 * Class which represents single topic post item in presentation layer.
 */
class SinglePostModel(
    val authorNickname: String,
    val authorProfile: String,
    val authorProfileId: Int,
    val authorAvatar: String,
    val authorMessagesCount: Int,
    val postDate: String,
    val postRank: Int,
    val postRankText: String,
    val postRankPlusAvailable: Boolean,
    val postRankMinusAvailable: Boolean,
    val postRankPlusClicked: Boolean,
    val postRankMinusClicked: Boolean,
    val postContentParsed: SinglePostParsedModel,
    val postId: Int
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.SINGLE_POST_ITEM
}
