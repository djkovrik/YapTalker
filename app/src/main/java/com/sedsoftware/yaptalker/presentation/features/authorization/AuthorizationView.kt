package com.sedsoftware.yaptalker.presentation.features.authorization

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface AuthorizationView : BaseView {

  fun updateCurrentUiState()

  fun loginSuccessMessage()

  fun loginErrorMessage()

  fun setSignInButtonState(isEnabled: Boolean)

  fun hideKeyboard()
}
