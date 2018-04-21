package com.sedsoftware.yaptalker.presentation.feature.news.adapter

interface NewsItemElementsClickListener {

  fun onNewsItemClicked(forumId: Int, topicId: Int)

  fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean)
}
