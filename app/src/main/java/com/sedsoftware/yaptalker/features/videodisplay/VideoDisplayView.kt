package com.sedsoftware.yaptalker.features.videodisplay

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface VideoDisplayView : MvpView {

  fun initWebView()

  fun displayWebViewContent()

  fun clearWebView()
}