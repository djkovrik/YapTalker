package com.sedsoftware.yaptalker.presentation.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BaseView : MvpView {

  @StateStrategyType(SkipStrategy::class)
  fun showErrorMessage(message: String)

  fun showLoadingIndicator() {
    // Default empty implementation
  }

  fun hideLoadingIndicator() {
    // Default empty implementation
  }
}
