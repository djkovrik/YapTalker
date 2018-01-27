package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents single topic post item in domain layer.
 */
class SinglePost(
  val authorNickname: String,
  val authorProfile: String,
  val authorAvatar: String,
  val authorMessagesCount: Int,
  val postDate: String,
  val postRank: Int,
  val postRankPlusAvailable: Boolean,
  val postRankMinusAvailable: Boolean,
  val postRankPlusClicked: Boolean,
  val postRankMinusClicked: Boolean,
  val postContentParsed: SinglePostParsed,
  val postId: Int,
  val hasQuoteButton: Boolean,
  val hasEditButton: Boolean
) : BaseEntity
