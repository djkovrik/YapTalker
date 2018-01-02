package com.sedsoftware.yaptalker.presentation.extensions

import android.widget.ImageView
import com.sedsoftware.yaptalker.presentation.utility.CircleImageTransformation
import com.sedsoftware.yaptalker.presentation.utility.ImageOverlayTransformation
import com.squareup.picasso.Picasso

fun ImageView.loadFromUrl(url: String) {
  Picasso
      .with(context)
      .load(url.validateUrl())
      .into(this)
}

fun ImageView.loadThumbnailFromUrl(url: String) {
  Picasso
      .with(context)
      .load(url.validateUrl())
      .transform(ImageOverlayTransformation(context))
      .into(this)
}

fun ImageView.loadAvatarFromUrl(url: String) {
  Picasso
      .with(context)
      .load(url.validateUrl())
      .transform(CircleImageTransformation())
      .into(this)
}
