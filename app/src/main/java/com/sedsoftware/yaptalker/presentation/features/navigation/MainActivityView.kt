package com.sedsoftware.yaptalker.presentation.features.navigation

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainActivityView : BaseView {

  fun setAppbarTitle(title: String)

  fun selectNavDrawerItem(item: Long)
}
