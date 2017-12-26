package com.sedsoftware.yaptalker.presentation.features.news.adapter

import android.widget.ImageView

interface NewsItemThumbnailsLoader {

  fun loadThumbnail(videoUrl: String, imageView: ImageView)
}
