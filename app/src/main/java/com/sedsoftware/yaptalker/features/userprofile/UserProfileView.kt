package com.sedsoftware.yaptalker.features.userprofile

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseViewWithLoading
import com.sedsoftware.yaptalker.data.model.UserProfile

@StateStrategyType(AddToEndSingleStrategy::class)
interface UserProfileView : BaseViewWithLoading {

  fun displayProfile(profile: UserProfile)
}
