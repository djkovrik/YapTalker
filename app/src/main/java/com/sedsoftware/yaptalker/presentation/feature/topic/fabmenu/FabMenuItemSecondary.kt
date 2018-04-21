package com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import android.view.animation.OvershootInterpolator
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.showView

class FabMenuItemSecondary(
  private val context: Context?,
  private val view: View
) : FabMenuItem {

  override fun show() {
    val animator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_vertical_show) as AnimatorSet
    animator.interpolator = OvershootInterpolator()
    animator.setTarget(view)
    animator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator?) {
        super.onAnimationStart(animation)
        view.showView()
      }
    })
    animator.start()
  }

  override fun hide() {
    val animator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_vertical_hide) as AnimatorSet
    animator.interpolator = OvershootInterpolator()
    animator.setTarget(view)
    animator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator?) {
        super.onAnimationEnd(animation)
        view.hideView()
      }
    })
    animator.start()
  }
}
