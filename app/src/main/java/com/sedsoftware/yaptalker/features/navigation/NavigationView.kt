package com.sedsoftware.yaptalker.features.navigation

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface NavigationView : BaseView {

  fun setAppbarTitle(title: String)

  fun showEula()
}
