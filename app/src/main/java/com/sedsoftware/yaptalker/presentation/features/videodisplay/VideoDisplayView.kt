package com.sedsoftware.yaptalker.presentation.features.videodisplay

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface VideoDisplayView : BaseView {

  fun initWebView()

  fun displayWebViewContent()

  fun clearWebView()
}
