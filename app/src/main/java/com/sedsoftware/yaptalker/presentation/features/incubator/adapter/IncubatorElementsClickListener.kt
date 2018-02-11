package com.sedsoftware.yaptalker.presentation.features.incubator.adapter

interface IncubatorElementsClickListener {

  fun onIncubatorItemClicked(forumId: Int, topicId: Int)

  fun onMediaPreviewClicked(url: String, html: String, isVideo: Boolean)
}
