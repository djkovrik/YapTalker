package com.sedsoftware.yaptalker.presentation.features.authorization

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastSuccess
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_authorization.authorization_anonymous
import kotlinx.android.synthetic.main.fragment_authorization.authorization_login
import kotlinx.android.synthetic.main.fragment_authorization.authorization_password
import kotlinx.android.synthetic.main.fragment_authorization.button_sign_in
import javax.inject.Inject

class AuthorizationFragment : BaseFragment(), AuthorizationView {

  companion object {
    fun getNewInstance() = AuthorizationFragment()
  }

  override val layoutId: Int
    get() = R.layout.fragment_authorization

  @Inject
  @InjectPresenter
  lateinit var presenter: AuthorizationPresenter

  @ProvidePresenter
  fun provideAuthPresenter() = presenter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    subscribeViews()
  }

  override fun updateCurrentUiState() {
    context?.stringRes(R.string.nav_drawer_sign_in)?.let { presenter.setAppbarTitle(it) }
    presenter.setNavDrawerItem(NavigationSection.SIGN_IN)
  }

  override fun loginSuccessMessage() {
    context?.stringRes(R.string.msg_login_success)?.let { toastSuccess(it) }
  }

  override fun loginErrorMessage() {
    context?.stringRes(R.string.msg_login_error)?.let { toastError(it) }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun setSignInButtonState(isEnabled: Boolean) {
    button_sign_in?.isEnabled = isEnabled
  }

  override fun hideKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
  }

  private fun subscribeViews() {

    Observable
      .combineLatest(
        RxTextView.textChanges(authorization_login),
        RxTextView.textChanges(authorization_password),
        BiFunction { login: CharSequence, password: CharSequence ->
          login.isNotEmpty() && password.isNotEmpty()
        })
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { enabled -> presenter.handleSignInButton(enabled) }

    RxView
      .clicks(button_sign_in)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe {
        presenter.performLoginAttempt(
          authorization_login.text.toString(),
          authorization_password.text.toString(),
          authorization_anonymous.isChecked
        )
      }
  }
}
