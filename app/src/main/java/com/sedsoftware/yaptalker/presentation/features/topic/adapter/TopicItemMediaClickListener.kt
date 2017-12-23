package com.sedsoftware.yaptalker.presentation.features.topic.adapter

interface TopicItemMediaClickListener {

  fun onPreviewClicked(url: String, html: String, isVideo: Boolean)
}