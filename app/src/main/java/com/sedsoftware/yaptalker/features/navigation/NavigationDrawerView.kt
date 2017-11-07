package com.sedsoftware.yaptalker.features.navigation

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.AuthorizedUserInfo

@StateStrategyType(SkipStrategy::class)
interface NavigationDrawerView : BaseView {

  fun updateNavDrawerProfile(userInfo: AuthorizedUserInfo)

  fun clearDynamicNavigationItems()

  fun displaySignedInNavigation()

  fun displaySignedOutNavigation()

  fun showSignOutMessage()
}
