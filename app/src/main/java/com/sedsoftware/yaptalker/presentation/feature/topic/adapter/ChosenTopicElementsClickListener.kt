package com.sedsoftware.yaptalker.presentation.feature.topic.adapter

import com.sedsoftware.yaptalker.presentation.model.base.TagModel

interface ChosenTopicElementsClickListener {

    fun onPostItemClicked(postId: Int, isKarmaAvailable: Boolean)

    fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean)

    fun onUserAvatarClicked(userId: Int)

    fun onReplyButtonClicked(authorNickname: String, postDate: String, postId: Int)

    fun onEditButtonClicked(postId: Int)

    fun onTopicTagClicked(tag: TagModel)
}
