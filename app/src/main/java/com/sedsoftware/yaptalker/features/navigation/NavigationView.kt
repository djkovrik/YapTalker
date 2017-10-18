package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface NavigationView : BaseView {

  fun initDrawer(savedInstanceState: Bundle?)

  fun goToChosenSection(@Navigation.Section section: Long)

  fun showSignOutMessage()

  fun goToMainPage()
}
