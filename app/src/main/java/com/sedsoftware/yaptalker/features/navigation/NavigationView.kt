package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface NavigationView : MvpView {

  @StateStrategyType(OneExecutionStateStrategy::class)
  fun initDrawer()

  @StateStrategyType(OneExecutionStateStrategy::class)
  fun initRouter(savedInstanceState: Bundle?)

  fun goToChosenSection(@Navigation.Section section: Long)
}