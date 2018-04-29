package com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapters

interface BookmarkElementsClickListener {

  fun onDeleteIconClick(bookmarkId: Int)

  fun onTopicItemClick(link: String)
}
