package com.sedsoftware.yaptalker.presentation.features.incubator.adapter

interface IncubatorElementsClickListener {

  fun onIncubatorItemClick(forumId: Int, topicId: Int)

  fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean)
}
