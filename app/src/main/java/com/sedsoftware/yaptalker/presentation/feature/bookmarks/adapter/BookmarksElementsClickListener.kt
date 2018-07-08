package com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapter

import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel

interface BookmarksElementsClickListener {

    fun onDeleteIconClick(item: BookmarkedTopicModel)

    fun onTopicItemClick(link: String)
}
