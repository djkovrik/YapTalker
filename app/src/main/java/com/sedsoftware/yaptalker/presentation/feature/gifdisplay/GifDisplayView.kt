package com.sedsoftware.yaptalker.presentation.feature.gifdisplay

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface GifDisplayView : BaseView {

  fun initWebView()

  fun displayWebViewContent()

  fun clearWebView()
}
