package com.sedsoftware.yaptalker.presentation.features.navigation

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationDrawerItems
import com.sedsoftware.yaptalker.presentation.base.navigation.RequestCodes
import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.GetLoginSessionInfo
import com.sedsoftware.yaptalker.domain.interactor.SendSignOutRequest
import com.sedsoftware.yaptalker.domain.interactor.SendSignOutRequest.Params
import com.sedsoftware.yaptalker.presentation.mappers.LoginSessionInfoModelMapper
import com.sedsoftware.yaptalker.presentation.mappers.ServerResponseModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel
import com.sedsoftware.yaptalker.presentation.model.base.ServerResponseModel
import io.reactivex.observers.DisposableObserver
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class NavigationPresenter @Inject constructor(
    private val router: Router,
    private val settings: SettingsManager,
    private val getSessionInfoUseCase: GetLoginSessionInfo,
    private val sessionInfoMapper: LoginSessionInfoModelMapper,
    private val signOutUseCase: SendSignOutRequest,
    private val serverResponseMapper: ServerResponseModelMapper
) : BasePresenter<NavigationView>() {

  companion object {
    private const val SIGN_OUT_SUCCESS_MARKER = "Вы вышли"
  }

  init {
    router.setResultListener(RequestCodes.SIGN_IN, {
      refreshAuthorization()
      navigateToDefaultMainPage()
    })
  }

  private var currentUserKey = ""

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    navigateToDefaultMainPage()
  }


  override fun attachView(view: NavigationView?) {
    super.attachView(view)
    refreshAuthorization()
  }

  override fun onDestroy() {
    super.onDestroy()
    getSessionInfoUseCase.dispose()
    signOutUseCase.dispose()
    router.removeResultListener(RequestCodes.SIGN_IN)
  }

  fun navigateToChosenSection(@NavigationDrawerItems.Section identifier: Long) {
    // TODO () Restore navdrawer menu navigation
    Timber.d("Navigate to $identifier")
  }

  fun navigateWithIntentLink(triple: Triple<Int, Int, Int>) {
    // TODO () Restore navigation to link
    // router.navigateTo(NavigationScreens.CHOSEN_TOPIC_SCREEN, triple)
  }

  private fun navigateToDefaultMainPage() {
    // TODO () Restore navigation to main page
    // router.newRootScreen(settings.getStartingPage())
  }

  private fun refreshAuthorization() {
    getSessionInfoUseCase.execute(getLoginSessionInfoObserver(), Unit)
  }

  private fun sendSignOutRequest() {
    signOutUseCase.execute(getSignOutResponseObserver(), Params(userKey = currentUserKey))
  }

  private fun getLoginSessionInfoObserver() =
      object : DisposableObserver<BaseEntity>() {
        override fun onComplete() {
          Timber.i("Login session info updated.")
        }

        override fun onNext(info: BaseEntity) {
          displayLoginSessionInfo(info)
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }

  private fun displayLoginSessionInfo(info: BaseEntity) {
    sessionInfoMapper
        .transform(info)
        .also { sessionInfo ->
          sessionInfo as LoginSessionInfoModel
          currentUserKey = sessionInfo.sessionId
          viewState.updateNavDrawerProfile(sessionInfo)

          if (sessionInfo.nickname.isEmpty()) {
            viewState.displaySignedOutNavigation()
          } else {

          }
        }
  }

  private fun getSignOutResponseObserver() =
      object : DisposableObserver<BaseEntity>() {
        override fun onComplete() {
          Timber.i("Signed Out request completed.")
        }

        override fun onNext(response: BaseEntity) {
          checkIfSignedOutSuccessfully(response)
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }

  private fun checkIfSignedOutSuccessfully(response: BaseEntity) {
    serverResponseMapper
        .transform(response)
        .also { serverResponse ->
          serverResponse as ServerResponseModel
          if (serverResponse.text.contains(SIGN_OUT_SUCCESS_MARKER)) {
            viewState.showSignOutMessage()
            refreshAuthorization()
            navigateToDefaultMainPage()
          }
        }
  }
}
