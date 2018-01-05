package com.sedsoftware.yaptalker.presentation.features.topic.fabmenu

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.showView

class FabMenuItemPrimary(
    private val context: Context?,
    private val mainView: View,
    private val secondaryView: View,
    private val secondaryViewLabel: View,
    private val showSecondaryView: Boolean
) : FabMenuItem {

  override fun show() {
    if (showSecondaryView) {
      rotateAndHideView(mainView)
      rotateAndShowView(secondaryView)
      secondaryViewLabel.showView()
    }
  }

  override fun hide() {
    if (showSecondaryView) {
      rotateAndHideView(secondaryView)
      rotateAndShowView(mainView)
      secondaryViewLabel.hideView()
    }
  }

  private fun rotateAndHideView(hideTarget: View) {
    val animator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_rotate_button_hide) as AnimatorSet
    animator.interpolator = OvershootInterpolator()
    animator.setTarget(hideTarget)
    animator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator?) {
        super.onAnimationEnd(animation)
        hideTarget.hideView()
      }
    })
    animator.start()
  }

  private fun rotateAndShowView(showTarget: View) {
    val animator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_rotate_button_show) as AnimatorSet
    animator.interpolator = AnticipateInterpolator()
    animator.setTarget(showTarget)
    animator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator?) {
        super.onAnimationStart(animation)
        showTarget.showView()
      }
    })
    animator.start()
  }
}
