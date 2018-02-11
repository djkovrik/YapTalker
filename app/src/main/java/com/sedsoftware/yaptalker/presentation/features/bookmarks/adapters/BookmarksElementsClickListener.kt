package com.sedsoftware.yaptalker.presentation.features.bookmarks.adapters

interface BookmarksElementsClickListener {

  fun onDeleteIconClick(bookmarkId: Int)

  fun onTopicItemClick(link: String)
}
