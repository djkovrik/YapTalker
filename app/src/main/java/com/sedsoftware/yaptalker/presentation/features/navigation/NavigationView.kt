package com.sedsoftware.yaptalker.presentation.features.navigation

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.base.AuthorizedUserInfoModel

@StateStrategyType(SkipStrategy::class)
interface NavigationView : BaseView {

  fun updateNavDrawerProfile(userInfo: AuthorizedUserInfoModel)

  fun clearDynamicNavigationItems()

  fun displaySignedInNavigation()

  fun displaySignedOutNavigation()

  fun showSignOutMessage()
}