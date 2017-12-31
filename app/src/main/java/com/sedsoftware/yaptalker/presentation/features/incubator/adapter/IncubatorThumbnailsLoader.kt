package com.sedsoftware.yaptalker.presentation.features.incubator.adapter

import android.widget.ImageView

interface IncubatorThumbnailsLoader {

  fun loadThumbnail(videoUrl: String, imageView: ImageView)
}
