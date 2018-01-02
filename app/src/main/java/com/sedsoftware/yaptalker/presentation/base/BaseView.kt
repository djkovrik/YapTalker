package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface BaseView : MvpView {

  fun showErrorMessage(message: String)

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun showLoadingIndicator() {
    // Default empty implementation
  }

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun hideLoadingIndicator() {
    // Default empty implementation
  }
}
