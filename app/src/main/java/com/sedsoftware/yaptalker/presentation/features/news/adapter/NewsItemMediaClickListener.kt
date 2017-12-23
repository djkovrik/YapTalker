package com.sedsoftware.yaptalker.presentation.features.news.adapter

interface NewsItemMediaClickListener {

  fun onPreviewClicked(url: String, html: String, isVideo: Boolean)
}
