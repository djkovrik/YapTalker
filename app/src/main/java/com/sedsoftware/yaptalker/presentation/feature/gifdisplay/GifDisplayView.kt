package com.sedsoftware.yaptalker.presentation.feature.gifdisplay

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage

@StateStrategyType(SkipStrategy::class)
interface GifDisplayView : MvpView, CanShowErrorMessage {

  fun initWebView()

  fun displayWebViewContent()

  fun clearWebView()
}
