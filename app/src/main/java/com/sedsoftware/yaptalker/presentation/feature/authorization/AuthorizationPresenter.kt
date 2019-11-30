package com.sedsoftware.yaptalker.presentation.feature.authorization

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.AuthorizationInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.uber.autodispose.kotlin.autoDisposable
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class AuthorizationPresenter @Inject constructor(
    private val router: Router,
    private val authorizationInteractor: AuthorizationInteractor,
    private val settings: Settings,
    private val schedulers: SchedulersProvider
) : BasePresenter<AuthorizationView>() {

    override fun attachView(view: AuthorizationView?) {
        super.attachView(view)
        viewState.updateCurrentUiState()
    }

    override fun detachView(view: AuthorizationView?) {
        viewState.hideKeyboard()
        super.detachView(view)
    }

    fun handleSignInButton(enabled: Boolean) {
        viewState.setSignInButtonState(enabled)
    }

    fun performLoginAttempt(userLogin: String, userPassword: String, isAnonymous: Boolean) {
        authorizationInteractor
            .sendSignInRequest(login = userLogin, password = userPassword, anonymously = isAnonymous)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                viewState.showLoginSuccessMessage()
                Timber.i("Sign In request completed, start site preferences loading...")
                loadSitePreferences()
            }, { e: Throwable ->
                Timber.e("Error: ${e.message}")
                viewState.showLoginErrorMessage()
            })
    }

    fun performLoginAttemptNew(userLogin: String, userPassword: String, shouldRemember: Boolean) {
        authorizationInteractor
            .sendSignInRequestNew(login = userLogin, password = userPassword)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                viewState.showLoginSuccessMessage()
                Timber.i("Sign In request completed, start site preferences loading...")
                if (shouldRemember) {
                    settings.saveLogin(userLogin)
                    settings.savePassword(userPassword)
                }
                loadSitePreferences()
            }, { e: Throwable ->
                Timber.e("Error: ${e.message}")
                viewState.showLoginErrorMessage()
            })
    }

    fun clearStoredAccount() {
        settings.saveLogin("")
        settings.savePassword("")
    }

    private fun loadSitePreferences() {
        authorizationInteractor
            .getSiteUserPreferences()
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                Timber.i("Site preferences loading completed.")
                router.exitWithResult(RequestCode.SIGN_IN, true)
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }
}
