package com.sedsoftware.yaptalker.presentation.model.base

import android.text.Spanned
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.ItemType

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
  val postDateFull: String,
  val postRank: Int,
  val postRankText: Spanned,
  val postRankPlusAvailable: Boolean,
  val postRankMinusAvailable: Boolean,
  val postRankPlusClicked: Boolean,
  val postRankMinusClicked: Boolean,
  val postContentParsed: SinglePostParsedModel,
  val postId: Int,
  val hasQuoteButton: Boolean,
  val hasEditButton: Boolean
) : YapEntity {

  override fun getBaseEntityType(): Int = ItemType.SINGLE_POST
}
