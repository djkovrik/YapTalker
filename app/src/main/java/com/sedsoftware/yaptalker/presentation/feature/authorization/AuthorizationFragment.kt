package com.sedsoftware.yaptalker.presentation.feature.authorization

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_authorization.authorization_remember
import kotlinx.android.synthetic.main.fragment_authorization.authorization_login
import kotlinx.android.synthetic.main.fragment_authorization.authorization_password
import kotlinx.android.synthetic.main.fragment_authorization.button_sign_in
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_authorization)
class AuthorizationFragment : BaseFragment(), AuthorizationView {

    companion object {
        fun getNewInstance(): AuthorizationFragment = AuthorizationFragment()
    }

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
        setCurrentAppbarTitle(string(R.string.nav_drawer_sign_in))
        setCurrentNavDrawerItem(NavigationSection.SIGN_IN)
    }

    override fun showLoginSuccessMessage() {
        messagesDelegate.showMessageSuccess(string(R.string.msg_login_success))
    }

    override fun showLoginErrorMessage() {
        messagesDelegate.showMessageError(string(R.string.msg_login_error))
    }

    override fun showErrorMessage(message: String) {
        messagesDelegate.showMessageError(message)
    }

    override fun setSignInButtonState(isEnabled: Boolean) {
        button_sign_in?.isEnabled = isEnabled
    }

    override fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun subscribeViews() {

        Observable.combineLatest(
            RxTextView.textChanges(authorization_login),
            RxTextView.textChanges(authorization_password),
            BiFunction { login: CharSequence, password: CharSequence ->
                login.isNotEmpty() && password.isNotEmpty()
            })
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({ enabled: Boolean ->
                presenter.handleSignInButton(enabled)
            }, { e: Throwable ->
                e.message?.let { showErrorMessage(it) }
            })

        RxView.clicks(authorization_remember)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({
                if (!authorization_remember.isChecked()) {
                    presenter.clearStoredAccount()
                }
            }, { e: Throwable ->
                e.message?.let { showErrorMessage(it) }
            })

        RxView.clicks(button_sign_in)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({
                authorization_login.isEnabled = false
                authorization_password.isEnabled = false
                authorization_remember.isEnabled = false
                presenter.performLoginAttemptNew(
                    authorization_login.text.toString(),
                    authorization_password.text.toString(),
                    authorization_remember.isChecked()
                )
            }, { e: Throwable ->
                e.message?.let { showErrorMessage(it) }
                authorization_login.isEnabled = true
                authorization_password.isEnabled = true
                authorization_remember.isEnabled = true
            })
    }
}
