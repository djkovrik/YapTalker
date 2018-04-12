package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "LoadingIndicator")
interface BaseLoadingView : BaseView {

  fun showLoadingIndicator()

  fun hideLoadingIndicator()
}
