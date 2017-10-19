package com.sedsoftware.yaptalker.features.authorization

import android.app.Activity
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseActivity
import com.sedsoftware.yaptalker.base.events.ActivityLifecycle
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastSuccess
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_authorization.*
import kotlinx.android.synthetic.main.include_main_appbar.*

// TODO () Fix EditText leak
class AuthorizationActivity : BaseActivity(), AuthorizationView {

  @InjectPresenter
  lateinit var authorizationPresenter: AuthorizationPresenter

  override val layoutId: Int
    get() = R.layout.activity_authorization

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    Observable
        .combineLatest(
            RxTextView.textChanges(authorization_login),
            RxTextView.textChanges(authorization_password),
            BiFunction { login: CharSequence, password: CharSequence ->
              login.isNotEmpty() && password.isNotEmpty()
            })
        .autoDisposeWith(event(ActivityLifecycle.DESTROY))
        .subscribe { enabled -> authorizationPresenter.handleSignInButton(enabled) }

    RxView
        .clicks(button_sign_in)
        .autoDisposeWith(event(ActivityLifecycle.DESTROY))
        .subscribe {
          authorizationPresenter.loginAttempt(
              authorization_login.text.toString(),
              authorization_password.text.toString())
        }
  }

  override fun updateAppbarTitle(title: String) {

  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun loginSuccessMessage() {
    toastSuccess(stringRes(R.string.msg_login_success))
  }

  override fun loginErrorMessage() {
    toastError(stringRes(R.string.msg_login_error))
  }

  override fun signInButtonEnabled(enabled: Boolean) {
    button_sign_in?.isEnabled = enabled
  }

  override fun setResultToOk() {
    setResult(Activity.RESULT_OK)
  }

  override fun backToMainScreen() {
    finish()
  }
}
