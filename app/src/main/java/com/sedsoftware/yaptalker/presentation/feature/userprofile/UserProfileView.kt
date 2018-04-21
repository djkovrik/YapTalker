package com.sedsoftware.yaptalker.presentation.feature.userprofile

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.model.base.UserProfileModel

interface UserProfileView : MvpView, CanShowErrorMessage {

  @StateStrategyType(SkipStrategy::class)
  fun displayProfile(profile: UserProfileModel)

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateCurrentUiState(title: String)
}
