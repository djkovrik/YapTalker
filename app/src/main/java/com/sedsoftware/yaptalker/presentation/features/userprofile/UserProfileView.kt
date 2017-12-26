package com.sedsoftware.yaptalker.presentation.features.userprofile

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.base.UserProfileModel

interface UserProfileView : BaseView {

  @StateStrategyType(SkipStrategy::class)
  fun displayProfile(profile: UserProfileModel)

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateCurrentUiState(title: String)
}
