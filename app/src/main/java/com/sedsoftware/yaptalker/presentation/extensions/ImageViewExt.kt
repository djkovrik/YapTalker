package com.sedsoftware.yaptalker.presentation.extensions

import android.widget.ImageView
import com.sedsoftware.yaptalker.presentation.custom.CircleImageTransformation
import com.squareup.picasso.Picasso

fun ImageView.loadFromUrl(url: String) {
  Picasso
    .with(context)
    .load(url.validateUrl())
    .into(this)
}

fun ImageView.loadAvatarFromUrl(url: String) {
  Picasso
    .with(context)
    .load(url.validateUrl())
    .transform(CircleImageTransformation())
    .into(this)
}
