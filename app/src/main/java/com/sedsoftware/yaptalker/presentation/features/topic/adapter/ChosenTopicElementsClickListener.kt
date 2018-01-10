package com.sedsoftware.yaptalker.presentation.features.topic.adapter

interface ChosenTopicElementsClickListener {

  fun onPostItemClicked(postId: Int, isKarmaAvailable: Boolean)

  fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean)

  fun onGoToFirstPageClick()

  fun onGoToLastPageClick()

  fun onGoToPreviousPageClick()

  fun onGoToNextPageClick()

  fun onGoToSelectedPageClick()

  fun onUserAvatarClick(userId: Int)

  fun onReplyButtonClick(authorNickname: String, postDate: String, postId: Int)
}
