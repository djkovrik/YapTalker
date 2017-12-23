package com.sedsoftware.yaptalker.presentation.features.news.adapter

interface NewsItemElementsClickListener {

  fun onNewsItemClick(forumId: Int, topicId: Int)

  fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean)
}
