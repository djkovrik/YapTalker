package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@Suppress("EmptyFunctionBlock")
@StateStrategyType(SkipStrategy::class)
interface BaseView : MvpView {

  fun showErrorMessage(message: String) {
    // Default empty implementation
  }

  fun showLoadingIndicator() {
    // Default empty implementation
  }

  fun hideLoadingIndicator() {
    // Default empty implementation
  }
}
