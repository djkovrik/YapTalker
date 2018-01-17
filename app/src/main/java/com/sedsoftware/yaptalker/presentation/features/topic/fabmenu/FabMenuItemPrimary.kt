package com.sedsoftware.yaptalker.presentation.features.topic.fabmenu

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import android.view.animation.OvershootInterpolator
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.hideViewAsInvisible
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

      val hideAnimator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_rotate_button_hide) as AnimatorSet
      hideAnimator.interpolator = OvershootInterpolator()
      hideAnimator.setTarget(mainView)
      hideAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
          super.onAnimationEnd(animation)
          mainView.hideViewAsInvisible()
        }
      })
      hideAnimator.start()

      val showAnimator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_rotate_button_show) as AnimatorSet
      showAnimator.interpolator = OvershootInterpolator()
      showAnimator.setTarget(secondaryView)
      showAnimator.addListener(object : AnimatorListenerAdapter() {

        override fun onAnimationStart(animation: Animator?) {
          super.onAnimationStart(animation)
          secondaryView.showView()
          showLabelAnimated()
        }
      })
      showAnimator.start()
    }
  }

  override fun hide() {
    if (showSecondaryView) {
      val hideAnimator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_rotate_button_hide) as AnimatorSet
      hideAnimator.interpolator = OvershootInterpolator()
      hideAnimator.setTarget(secondaryView)
      hideAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
          super.onAnimationEnd(animation)
          secondaryView.hideViewAsInvisible()
        }
      })
      hideAnimator.start()

      val showAnimator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_rotate_button_show) as AnimatorSet
      showAnimator.interpolator = OvershootInterpolator()
      showAnimator.setTarget(mainView)
      showAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
          super.onAnimationStart(animation)
          mainView.showView()
          hideLabelAnimated()
        }
      })
      showAnimator.start()
    }
  }

  private fun showLabelAnimated() {
    val showAnimator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_label_show) as AnimatorSet
    showAnimator.interpolator = OvershootInterpolator()
    showAnimator.setTarget(secondaryViewLabel)
    showAnimator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator?) {
        super.onAnimationStart(animation)
        secondaryViewLabel.showView()
      }
    })
    showAnimator.start()
  }

  private fun hideLabelAnimated() {
    val hideAnimator = AnimatorInflater.loadAnimator(context, R.animator.fab_menu_label_hide) as AnimatorSet
    hideAnimator.interpolator = OvershootInterpolator()
    hideAnimator.setTarget(secondaryViewLabel)
    hideAnimator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator?) {
        super.onAnimationEnd(animation)
        secondaryViewLabel.hideViewAsInvisible()
      }
    })
    hideAnimator.start()
  }
}
