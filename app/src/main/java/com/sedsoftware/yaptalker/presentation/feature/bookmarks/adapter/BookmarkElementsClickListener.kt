package com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapter

interface BookmarkElementsClickListener {

  fun onDeleteIconClick(bookmarkId: Int)

  fun onTopicItemClick(link: String)
}
