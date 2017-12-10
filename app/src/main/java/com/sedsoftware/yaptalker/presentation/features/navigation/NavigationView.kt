package com.sedsoftware.yaptalker.presentation.features.navigation

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel

@StateStrategyType(SkipStrategy::class)
interface NavigationView : BaseView {

  fun updateNavDrawerProfile(userInfo: LoginSessionInfoModel)

  fun clearDynamicNavigationItems()

  fun displaySignedInNavigation()

  fun displaySignedOutNavigation()

  fun showSignOutMessage()
}
