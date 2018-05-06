package com.sedsoftware.yaptalker.presentation.thumbnail

import android.widget.ImageView

interface ThumbnailsLoader {

  fun loadThumbnail(videoUrl: String, imageView: ImageView)
}
