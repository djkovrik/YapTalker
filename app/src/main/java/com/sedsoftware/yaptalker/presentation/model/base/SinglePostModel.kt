package com.sedsoftware.yaptalker.presentation.model.base

import android.text.Spanned
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

data class SinglePostModel(
    val authorNickname: String,
    val authorProfile: String,
    val authorProfileId: Int,
    val authorAvatar: String,
    val authorMessagesCount: Int,
    val postDate: String,
    val postDateFull: String,
    var isTopicStarterHere: Boolean,
    var postRank: Int,
    var postRankText: Spanned,
    var postRankPlusAvailable: Boolean,
    var postRankMinusAvailable: Boolean,
    var postRankPlusClicked: Boolean,
    var postRankMinusClicked: Boolean,
    val postContentParsed: SinglePostParsedModel,
    val postId: Int,
    val hasQuoteButton: Boolean,
    val hasEditButton: Boolean,
    val tags: List<TagModel>
) : DisplayedItemModel {

    override fun getEntityType(): Int = DisplayedItemType.SINGLE_POST
}
