package com.sedsoftware.yaptalker.presentation.feature.authorization

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage

@StateStrategyType(SkipStrategy::class)
interface AuthorizationView : MvpView, CanShowErrorMessage {

  fun updateCurrentUiState()

  fun loginSuccessMessage()

  fun loginErrorMessage()

  fun setSignInButtonState(isEnabled: Boolean)

  fun hideKeyboard()
}
