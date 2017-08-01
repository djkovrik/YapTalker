package com.sedsoftware.yaptalker.features.homeview

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType


@StateStrategyType(AddToEndSingleStrategy::class)
interface HomeView : MvpView {

  @StateStrategyType(OneExecutionStateStrategy::class)
  fun initNavigationDrawer()
}