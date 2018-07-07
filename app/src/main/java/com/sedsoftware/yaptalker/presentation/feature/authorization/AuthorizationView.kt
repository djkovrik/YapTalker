package com.sedsoftware.yaptalker.presentation.feature.authorization

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState

@StateStrategyType(SkipStrategy::class)
interface AuthorizationView : MvpView, CanShowErrorMessage, CanUpdateUiState {

    fun showLoginSuccessMessage()

    fun showLoginErrorMessage()

    fun setSignInButtonState(isEnabled: Boolean)

    fun hideKeyboard()
}
