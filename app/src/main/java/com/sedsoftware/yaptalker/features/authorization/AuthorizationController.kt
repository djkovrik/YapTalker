package com.sedsoftware.yaptalker.features.authorization

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.scopeProvider
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastSuccess
import com.sedsoftware.yaptalker.features.base.BaseController
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.controller_authorization.view.*

class AuthorizationController : BaseController(), AuthorizationView {

  @InjectPresenter
  lateinit var authorizationPresenter: AuthorizationPresenter

  override val controllerLayoutId: Int
    get() = R.layout.controller_authorization

  override fun onViewBound(view: View, savedViewState: Bundle?) {
  }

  override fun subscribeViews(parent: View) {

    Observable
        .combineLatest(
            RxTextView.textChanges(parent.authorization_login),
            RxTextView.textChanges(parent.authorization_password),
            BiFunction { login: CharSequence, password: CharSequence -> login.isNotEmpty() && password.isNotEmpty() })
        .autoDisposeWith(scopeProvider)
        .subscribe { enabled -> authorizationPresenter.handleSignInButton(enabled) }

    parent.button_sign_in?.let {
      RxView
          .clicks(parent.button_sign_in)
          .autoDisposeWith(scopeProvider)
          .subscribe {
            authorizationPresenter.loginAttempt(
                parent.authorization_login.text.toString(),
                parent.authorization_password.text.toString())
          }
    }
  }

  override fun updateAppbarTitle() {
    view?.context?.let {
      authorizationPresenter.updateTitle(it.stringRes(R.string.nav_drawer_sign_in))
    }
  }

  override fun loginSuccessMessage() {
    view?.let {
      toastSuccess(it.context.stringRes(R.string.msg_login_success))
    }
  }

  override fun loginErrorMessage() {
    view?.let {
      toastError(it.context.stringRes(R.string.msg_login_error))
    }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun signInButtonEnabled(enabled: Boolean) {
    view?.button_sign_in?.isEnabled = enabled
  }
}