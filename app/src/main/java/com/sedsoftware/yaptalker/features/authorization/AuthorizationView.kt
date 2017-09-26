package com.sedsoftware.yaptalker.features.authorization

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface AuthorizationView : MvpView {

  fun updateAppbarTitle()

  fun loginSuccessMessage()

  fun loginErrorMessage()

  fun showErrorMessage(message: String)

  fun signInButtonEnabled(enabled: Boolean)

  fun setResultToOk()

  fun backToMainScreen()
}
