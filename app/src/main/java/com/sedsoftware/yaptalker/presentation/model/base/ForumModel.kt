package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType

data class ForumModel(
    val title: String,
    val forumId: Int,
    val lastTopicTitle: String,
    val lastTopicAuthor: String,
    val date: String
) : DisplayedItemModel {

    override fun getEntityType(): Int = DisplayedItemType.FORUM
}
