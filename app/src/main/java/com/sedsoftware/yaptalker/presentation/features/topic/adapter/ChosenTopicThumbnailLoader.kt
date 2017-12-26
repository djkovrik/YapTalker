package com.sedsoftware.yaptalker.presentation.features.topic.adapter

import android.widget.ImageView

interface ChosenTopicThumbnailLoader {

  fun loadThumbnail(videoUrl: String, imageView: ImageView)
}
