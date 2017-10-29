package com.sedsoftware.yaptalker.features.authorization

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseFragment
import com.sedsoftware.yaptalker.base.events.FragmentLifecycle
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastSuccess
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_authorization.*

class AuthorizationFragment : BaseFragment(), AuthorizationView {

  companion object {
    fun getNewInstance() = AuthorizationFragment()
  }

  @InjectPresenter
  lateinit var authorizationPresenter: AuthorizationPresenter

  override val layoutId: Int
    get() = R.layout.fragment_authorization

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    authorizationPresenter.updateAppbarTitle(context.stringRes(R.string.nav_drawer_sign_in))
  }

  override fun subscribeViews() {

    Observable
        .combineLatest(
            RxTextView.textChanges(authorization_login),
            RxTextView.textChanges(authorization_password),
            BiFunction { login: CharSequence, password: CharSequence ->
              login.isNotEmpty() && password.isNotEmpty()
            })
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { enabled -> authorizationPresenter.handleSignInButton(enabled) }

    RxView
        .clicks(button_sign_in)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe {
          authorizationPresenter.loginAttempt(
              authorization_login.text.toString(),
              authorization_password.text.toString())
        }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun loginSuccessMessage() {
    toastSuccess(context.stringRes(R.string.msg_login_success))
  }

  override fun loginErrorMessage() {
    toastError(context.stringRes(R.string.msg_login_error))
  }

  override fun signInButtonEnabled(enabled: Boolean) {
    button_sign_in?.isEnabled = enabled
  }
}
