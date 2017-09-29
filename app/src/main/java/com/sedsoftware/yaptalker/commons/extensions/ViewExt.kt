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

/**
 * Extension property for TextView text color.
 */
var TextView.textColor: Int
  get() = currentTextColor
  set(v) = setTextColor(context.color(v))

/**
 * Extension property to get View bottom margin.
 */
val View.bottomMargin: Int
  get() {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    return layoutParams.bottomMargin
  }

/**
 * Loads image into ImageView
 *
 * @param url Image url.
 */
fun ImageView.loadFromUrl(url: String) {

  Picasso
      .with(context)
      .load(url)
      .into(this)
}

/**
 * Loads avatar into ImageView and makes it circle.
 *
 * @param url Avatar url.
 */
fun ImageView.loadAvatarFromUrl(url: String) {

  Picasso
      .with(context)
      .load(url)
      .transform(CircleImageTransformation())
      .into(this)
}

/**
 * Loads drawable into ImageView.
 *
 * @param resId Drawable resource id.
 */
fun ImageView.loadFromDrawable(resId: Int) {
  Picasso.with(context).load(resId).into(this)
}

/**
 * Sets html formatted text to TextView.
 *
 * @param html Source html.
 */
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

/**
 * Sets html formatted text to TextView, supports emoji.
 *
 * @param html Source html.
 */
@Suppress("DEPRECATION")
fun TextView.textFromHtmlWithEmoji(html: String) {
  this.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, PicassoImageGetter(context, this), null)
  } else {
    Html.fromHtml(html, PicassoImageGetter(context, this), null)
  }
}

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

/**
 * Hides view beyond screen edge.
 *
 * @param offset Y-axis offset for view animation.
 * @param delay Animation starting delay.
 * @param interpolator Animation interpolator.
 */
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

/**
 * Returns previously hidden view to position with zero Y-axis offset.
 *
 * @param delay Animation starting delay.
 * @param interpolator Animation interpolator.
 */
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

/**
 * Hides view from the screen with no animation.
 */
fun View.hideView() {
  this.visibility = View.GONE
}

/**
 * Makes previously hidden view visible with no animation.
 */
fun View.showView() {
  this.visibility = View.VISIBLE
}
