package com.sedsoftware.yaptalker.presentation.provider

import android.widget.ImageView

interface ThumbnailsProvider {

    fun loadThumbnail(videoUrl: String, imageView: ImageView)
}
