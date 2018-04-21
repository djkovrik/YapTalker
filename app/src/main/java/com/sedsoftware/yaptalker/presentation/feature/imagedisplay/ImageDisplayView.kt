package com.sedsoftware.yaptalker.presentation.feature.imagedisplay

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage

@StateStrategyType(SkipStrategy::class)
interface ImageDisplayView : MvpView, CanShowErrorMessage {

  fun fileSavedMessage(filepath: String)

  fun fileNotSavedMessage()
}
