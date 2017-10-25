package com.sedsoftware.yaptalker.features.videodisplay

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface VideoDisplayView : BaseView {

  fun initWebView()

  fun displayWebViewContent()

  fun clearWebView()
}
