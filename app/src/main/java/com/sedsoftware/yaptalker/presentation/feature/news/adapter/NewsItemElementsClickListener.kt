package com.sedsoftware.yaptalker.presentation.feature.news.adapter

import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel

interface NewsItemElementsClickListener {

    fun onNewsItemClicked(forumId: Int, topicId: Int)

    fun onNewsItemLongClicked(item: NewsItemModel)

    fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean)
}
