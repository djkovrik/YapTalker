package com.sedsoftware.yaptalker.features.authorization

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.commons.enums.PresenterLifecycle
import com.sedsoftware.yaptalker.features.base.BasePresenter
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber

@InjectViewState
class AuthorizationPresenter : BasePresenter<AuthorizationView>() {

  companion object {
    private const val ERROR_MESSAGE = "Обнаружены следующие ошибки"
    private const val SUCCESS_MESSAGE = "Спасибо"
  }

  override fun attachView(view: AuthorizationView?) {
    super.attachView(view)
    viewState.updateAppbarTitle()
  }

  fun handleSignInButton(enabled: Boolean) {
    viewState.signInButtonEnabled(enabled)
  }

  fun updateTitle(title: String) {
    pushAppbarTitle(titleChannel, title)
  }

  fun loginAttempt(userLogin: String, userPassword: String) {
    yapDataManager
        .loginToSite(login = userLogin, password = userPassword)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // On Success
          response ->
          onResponseReceived(response)
        }, {
          // On Error
          error ->
          Timber.d("Login error: ${error.message}")
        })
  }

  private fun onResponseReceived(response: Response<ResponseBody>) {

    response.body()?.string()?.let { str ->
      if (str.contains(ERROR_MESSAGE)) {
        viewState.loginErrorMessage()
      } else if (str.contains(SUCCESS_MESSAGE)){
        viewState.loginSuccessMessage()
        viewState.backToMainScreen()
      }
    }
  }
}
