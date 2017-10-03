package com.sedsoftware.yaptalker.commons.extensions

import android.support.v4.widget.SwipeRefreshLayout
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.CircleImageTransformation
import com.sedsoftware.yaptalker.commons.PicassoImageGetter
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// Fab params
private const val ANIMATION_DELAY_DEFAULT = 150L
private const val ANIMATION_DURATION = 250L
private const val DEFAULT_INTERPOLATOR_TENSION = 1.5f

var TextView.textColor: Int
  get() = currentTextColor
  set(v) = setTextColor(context.color(v))

val View.bottomMargin: Int
  get() {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    return layoutParams.bottomMargin
  }

fun ImageView.loadFromUrl(url: String) {

  Picasso
      .with(context)
      .load(url.validateURL())
      .into(this)
}

fun ImageView.loadAvatarFromUrl(url: String) {

  Picasso
      .with(context)
      .load(url.validateURL())
      .transform(CircleImageTransformation())
      .into(this)
}

fun ImageView.loadFromDrawable(resId: Int) {
  Picasso.with(context).load(resId).into(this)
}

@Suppress("DEPRECATION")
fun TextView.textFromHtml(html: String) {

  Single
      .just(html)
      .map { text ->
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
          Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
          Html.fromHtml(text)
        }
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeOn(Schedulers.computation())
      .subscribe { str: Spanned, _: Throwable? -> this.text = str }
}

@Suppress("DEPRECATION")
fun TextView.textFromHtmlWithEmoji(html: String) {
  this.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, PicassoImageGetter(context, this), null)
  } else {
    Html.fromHtml(html, PicassoImageGetter(context, this), null)
  }
}

fun SwipeRefreshLayout.setIndicatorColorScheme() {
  setColorSchemeColors(
      context.color(R.color.colorPrimary),
      context.color(R.color.colorPrimaryDark),
      context.color(R.color.colorAccent),
      context.color(R.color.colorAccentDark))
}

fun View.hideBeyondScreenEdge(
    offset: Float,
    delay: Long = ANIMATION_DELAY_DEFAULT,
    interpolator: Interpolator = OvershootInterpolator(DEFAULT_INTERPOLATOR_TENSION)) {

  this.animate()
      .translationY(offset)
      .setInterpolator(interpolator)
      .setStartDelay(delay)
      .setDuration(ANIMATION_DURATION)
      .start()
}

fun View.showFromScreenEdge(
    delay: Long = ANIMATION_DELAY_DEFAULT,
    interpolator: Interpolator = OvershootInterpolator(DEFAULT_INTERPOLATOR_TENSION)) {

  this.animate()
      .translationY(0f)
      .setInterpolator(interpolator)
      .setStartDelay(delay)
      .setDuration(ANIMATION_DURATION)
      .start()
}

fun View.hideView() {
  this.visibility = View.GONE
}

fun View.showView() {
  this.visibility = View.VISIBLE
}
