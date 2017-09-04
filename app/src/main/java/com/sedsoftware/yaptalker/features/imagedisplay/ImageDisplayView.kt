package com.sedsoftware.yaptalker.features.imagedisplay

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ImageDisplayView : MvpView {

  fun hideSystemUi()

  fun showSystemUi()
}