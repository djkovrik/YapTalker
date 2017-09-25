package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.data.model.UserInfo

@StateStrategyType(AddToEndSingleStrategy::class)
interface NavigationView : MvpView {

  @StateStrategyType(OneExecutionStateStrategy::class)
  fun initDrawer(savedInstanceState: Bundle?)

  @StateStrategyType(OneExecutionStateStrategy::class)
  fun goToChosenSection(@Navigation.Section section: Long)

  @StateStrategyType(OneExecutionStateStrategy::class)
  fun setActiveProfile(userInfo: UserInfo)

  fun setAppbarTitle(text: String)
}
