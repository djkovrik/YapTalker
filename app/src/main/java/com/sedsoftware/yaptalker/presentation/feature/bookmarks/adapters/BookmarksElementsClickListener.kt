package com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapters

interface BookmarksElementsClickListener {

  fun onDeleteIconClick(bookmarkId: Int)

  fun onTopicItemClick(link: String)
}
