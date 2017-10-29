package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.model.AuthorizedUserInfo

@StateStrategyType(OneExecutionStateStrategy::class)
interface NavigationView : BaseView {

  fun initDrawer(savedInstanceState: Bundle?)

  fun updateNavDrawer(userInfo: AuthorizedUserInfo)

  fun setAppbarTitle(title: String)

  fun showSignOutMessage()
}
