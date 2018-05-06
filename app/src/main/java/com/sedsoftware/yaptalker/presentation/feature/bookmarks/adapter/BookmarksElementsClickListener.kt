package com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapter

interface BookmarksElementsClickListener {

  fun onDeleteIconClick(bookmarkId: Int)

  fun onTopicItemClick(link: String)
}
