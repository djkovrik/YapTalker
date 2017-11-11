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

  }

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateAppbarTitle(title: String) {

  }

  fun highlightCurrentNavDrawerItem() {

  }

  fun showErrorMessage(message: String) {

  }

  fun showLoadingIndicator() {

  }

  fun hideLoadingIndicator() {

  }
}
