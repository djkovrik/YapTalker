package com.sedsoftware.yaptalker.features.imagedisplay

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface ImageDisplayView : BaseView {

  fun toggleSystemUiVisibility()

  fun hideAppbar()

  fun showAppbar()

  fun fileSavedMessage(filepath: String)

  fun fileNotSavedMessage()
}
