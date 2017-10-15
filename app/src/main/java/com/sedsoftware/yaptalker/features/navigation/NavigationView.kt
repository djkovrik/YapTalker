package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface NavigationView : MvpView {

  fun initDrawer(savedInstanceState: Bundle?)

  fun goToChosenSection(@Navigation.Section section: Long)

  //fun setActiveProfile(event: UpdateNavDrawerEvent)

  //fun setAppbarTitle(text: String)

  fun showSignOutMessage()

  fun goToMainPage()
}
