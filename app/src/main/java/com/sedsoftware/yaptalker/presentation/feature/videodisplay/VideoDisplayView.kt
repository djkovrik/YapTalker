package com.sedsoftware.yaptalker.presentation.feature.videodisplay

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage

@StateStrategyType(SkipStrategy::class)
interface VideoDisplayView : MvpView, CanShowErrorMessage {

    fun initWebView()

    fun displayWebViewContent()

    fun clearWebView()

    fun forceLandscapeOrientation()
}
