package com.sedsoftware.yaptalker.features.userprofile

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.UserProfile

@StateStrategyType(SkipStrategy::class)
interface UserProfileView : BaseView {

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateAppbarTitle(title: String)

  fun displayProfile(profile: UserProfile)
}
