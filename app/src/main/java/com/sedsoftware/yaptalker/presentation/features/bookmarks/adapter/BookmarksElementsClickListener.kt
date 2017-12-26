package com.sedsoftware.yaptalker.presentation.features.bookmarks.adapter

interface BookmarksElementsClickListener {

  fun onDeleteIconClick(bookmarkId: Int)

  fun onTopicClick(link: String)
}
