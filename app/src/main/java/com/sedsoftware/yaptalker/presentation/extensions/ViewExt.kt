package com.sedsoftware.yaptalker.presentation.extensions

import android.view.View
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator

private const val ANIMATION_DELAY_DEFAULT = 150L
private const val ANIMATION_DURATION = 250L

fun View.moveWithAnimationAxisY(
  offset: Float,
  delay: Long = ANIMATION_DELAY_DEFAULT,
  interpolator: Interpolator = OvershootInterpolator()
) {

  this.animate()
    .translationY(offset)
    .setInterpolator(interpolator)
    .setStartDelay(delay)
    .setDuration(ANIMATION_DURATION)
    .start()
}

fun View.hideView() {
  this.visibility = View.GONE
}

fun View.hideViewAsInvisible() {
  this.visibility = View.INVISIBLE
}

fun View.showView() {
  this.visibility = View.VISIBLE
}
