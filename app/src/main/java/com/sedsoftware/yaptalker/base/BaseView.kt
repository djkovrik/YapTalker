package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@Suppress("EmptyFunctionBlock")
@StateStrategyType(SkipStrategy::class)
interface BaseView : MvpView {

  fun updateAppbarTitle() {

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
