package com.sedsoftware.yaptalker.features.authorization

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface AuthorizationView : BaseView {

  fun loginSuccessMessage()

  fun loginErrorMessage()

  fun signInButtonEnabled(enabled: Boolean)

  fun hideKeyboard()
}
