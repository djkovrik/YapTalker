package com.sedsoftware.yaptalker.presentation.extensions

import android.widget.ImageView
import com.sedsoftware.yaptalker.presentation.custom.CircleImageTransformation
import com.sedsoftware.yaptalker.presentation.custom.RoundedTransformation
import com.squareup.picasso.Picasso

fun ImageView.loadFromUrl(url: String) {
  Picasso
    .with(context)
    .load(url.validateUrl())
    .into(this)
}

fun ImageView.loadFromUrlAndRoundCorners(url: String) {
  Picasso
    .with(context)
    .load(url.validateUrl())
    .transform(RoundedTransformation(radius = 5f))
    .into(this)
}

fun ImageView.loadAvatarFromUrl(url: String) {
  Picasso
    .with(context)
    .load(url.validateUrl())
    .transform(CircleImageTransformation())
    .into(this)
}
