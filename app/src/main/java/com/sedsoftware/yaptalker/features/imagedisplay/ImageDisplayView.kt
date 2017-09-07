package com.sedsoftware.yaptalker.features.imagedisplay

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface ImageDisplayView : MvpView {

  fun toggleSystemUiVisibility()

  fun hideAppbar()

  fun showAppbar()

  fun updateGallery(filepath: String)

  // TODO() Add file saving toast messages
}