package com.sedsoftware.yaptalker.presentation.feature.authorization

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.AuthorizationInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class AuthorizationPresenter @Inject constructor(
    private val router: Router,
    private val authorizationInteractor: AuthorizationInteractor
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                viewState.showLoginSuccessMessage()
                Timber.i("Sign In request completed, start site preferences loading...")
                loadSitePreferences()
            }, { viewState.showLoginErrorMessage() })
    }

    private fun loadSitePreferences() {
        authorizationInteractor
            .getSiteUserPreferences()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                Timber.i("Site preferences loading completed.")
                router.exitWithResult(RequestCode.SIGN_IN, true)
            }, { error ->
                error.message?.let { viewState.showErrorMessage(it) }
            })
    }
}
