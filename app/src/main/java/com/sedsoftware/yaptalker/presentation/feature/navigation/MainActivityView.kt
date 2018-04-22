package com.sedsoftware.yaptalker.presentation.feature.navigation

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel

@StateStrategyType(SkipStrategy::class)
interface MainActivityView : MvpView {

  fun updateNavDrawerProfile(userInfo: LoginSessionInfoModel)

  fun clearDynamicNavigationItems()

  fun displaySignedInNavigation()

  fun displaySignedOutNavigation()

  fun closeNavigationDrawer()
}
