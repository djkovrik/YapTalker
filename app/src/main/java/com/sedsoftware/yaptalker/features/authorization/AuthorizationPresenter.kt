package com.sedsoftware.yaptalker.features.authorization

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

@InjectViewState
class AuthorizationPresenter : BasePresenter<AuthorizationView>() {

  companion object {
    private const val ERROR_MESSAGE = "Обнаружены следующие ошибки"
    private const val SUCCESS_MESSAGE = "Спасибо"
  }

  fun handleSignInButton(enabled: Boolean) {
    viewState.signInButtonEnabled(enabled)
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
          error.message?.let { viewState.showErrorMessage(it) }
        })
  }

  private fun onResponseReceived(response: Response<ResponseBody>) {

    response.body()?.string()?.let { str ->
      if (str.contains(ERROR_MESSAGE)) {
        viewState.loginErrorMessage()
      } else if (str.contains(SUCCESS_MESSAGE)) {
        viewState.loginSuccessMessage()
        viewState.setResultToOk()
        viewState.backToMainScreen()
      }
    }
  }
}
