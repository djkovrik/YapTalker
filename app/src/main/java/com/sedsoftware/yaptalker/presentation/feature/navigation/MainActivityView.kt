package com.sedsoftware.yaptalker.presentation.feature.navigation

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainActivityView : MvpView {

  fun setAppbarTitle(title: String)

  fun selectNavDrawerItem(item: Long)
}
