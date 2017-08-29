package com.sedsoftware.yaptalker.commons.extensions

import android.support.v4.widget.SwipeRefreshLayout
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.CircleImageTransformation
import com.squareup.picasso.Picasso

private const val ICON_SIZE = 24

fun ImageView.loadFromUrl(url: String) {

  val placeholder = IconicsDrawable(context)
      .icon(CommunityMaterial.Icon.cmd_image)
      .color(context.color(R.color.colorPrimaryLight))
      .sizeDp(ICON_SIZE)

  Picasso.with(context).load(url).error(placeholder).into(this)
}

fun ImageView.loadAvatarFromUrl(url: String) {

  val placeholder = IconicsDrawable(context)
      .icon(CommunityMaterial.Icon.cmd_face_profile)
      .color(context.color(R.color.colorPrimaryLight))
      .sizeDp(ICON_SIZE)

  Picasso
      .with(context)
      .load(url)
      .transform(CircleImageTransformation())
      .error(placeholder)
      .into(this)
}

fun ImageView.loadFromDrawable(resId: Int) {
  Picasso.with(context).load(resId).into(this)
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

var TextView.textColor: Int
  get() = currentTextColor
  set(v) = setTextColor(context.color(v))

/**
 * Sets up SwipeRefreshLayout indicator coloring
 */
fun SwipeRefreshLayout.setAppColorScheme() {
  setColorSchemeColors(
      context.color(R.color.colorPrimary),
      context.color(R.color.colorPrimaryDark),
      context.color(R.color.colorAccent),
      context.color(R.color.colorAccentDark))
}
