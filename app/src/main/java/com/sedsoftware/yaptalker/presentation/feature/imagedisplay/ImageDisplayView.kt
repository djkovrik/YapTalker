package com.sedsoftware.yaptalker.presentation.feature.imagedisplay

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface ImageDisplayView : BaseView {

  fun fileSavedMessage(filepath: String)

  fun fileNotSavedMessage()
}
