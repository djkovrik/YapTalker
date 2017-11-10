package com.sedsoftware.yaptalker.commons.extensions

import android.view.View
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator

// Animation params
private const val ANIMATION_DELAY_DEFAULT = 150L
private const val ANIMATION_DURATION = 250L
private const val DEFAULT_INTERPOLATOR_TENSION = 1.5f

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
