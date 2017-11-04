package com.sedsoftware.yaptalker.features.userprofile

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.UserProfile

@StateStrategyType(SkipStrategy::class)
interface UserProfileView : BaseView {

  fun displayProfile(profile: UserProfile)
}
