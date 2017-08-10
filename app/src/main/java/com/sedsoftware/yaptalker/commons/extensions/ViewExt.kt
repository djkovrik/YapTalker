package com.sedsoftware.yaptalker.commons.extensions

import android.text.Html
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.sedsoftware.yaptalker.R
import com.squareup.picasso.Picasso

fun ImageView.loadFromUrl(url: String) {

  // TODO() Add placeholders
  if (TextUtils.isEmpty(url)) {
    Picasso.with(context).load(R.mipmap.ic_launcher).into(this)
  } else {
    Picasso.with(context).load(url).into(this)
  }
}

@Suppress("DEPRECATION")
fun TextView.textFromHtml(html: String) {

  this.text =
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
      } else {
        Html.fromHtml(html)
      }
}