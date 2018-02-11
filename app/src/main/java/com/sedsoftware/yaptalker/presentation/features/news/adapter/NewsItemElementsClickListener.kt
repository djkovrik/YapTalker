package com.sedsoftware.yaptalker.presentation.features.news.adapter

interface NewsItemElementsClickListener {

  fun onNewsItemClicked(forumId: Int, topicId: Int)

  fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean)
}
