package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@Suppress("EmptyFunctionBlock")
@StateStrategyType(SkipStrategy::class)
interface BaseView : MvpView {

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateAppbarTitle() {
    // Default empty implementation
  }

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateAppbarTitle(title: String) {
    // Default empty implementation
  }

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
