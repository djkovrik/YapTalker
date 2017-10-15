package com.sedsoftware.yaptalker.features.userprofile

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.data.model.UserProfile

@StateStrategyType(AddToEndSingleStrategy::class)
interface UserProfileView : MvpView {

  fun showLoading()

  fun showContent()

  fun showErrorMessage(message: String)

  fun displayProfile(profile: UserProfile)
}
