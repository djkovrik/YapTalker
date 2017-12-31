package com.sedsoftware.yaptalker.presentation.features.navigation

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.settings.DefaultHomeScreen
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.GetLoginSessionInfo
import com.sedsoftware.yaptalker.domain.interactor.SendSignOutRequest
import com.sedsoftware.yaptalker.domain.interactor.SendSignOutRequest.Params
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.sedsoftware.yaptalker.presentation.mappers.LoginSessionInfoModelMapper
import com.sedsoftware.yaptalker.presentation.mappers.ServerResponseModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel
import com.sedsoftware.yaptalker.presentation.model.base.ServerResponseModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
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
    router.setResultListener(RequestCode.SIGN_IN, {
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
    router.removeResultListener(RequestCode.SIGN_IN)
  }

  fun navigateToChosenSection(@NavigationSection.Section identifier: Long) {
    when (identifier) {
      NavigationSection.MAIN_PAGE -> router.newRootScreen(NavigationScreen.NEWS_SCREEN)
      NavigationSection.ACTIVE_TOPICS -> router.newRootScreen(NavigationScreen.ACTIVE_TOPICS_SCREEN)
      NavigationSection.INCUBATOR -> router.newRootScreen(NavigationScreen.INCUBATOR_SCREEN)
      NavigationSection.SIGN_IN -> router.navigateTo(NavigationScreen.AUTHORIZATION_SCREEN)
      NavigationSection.SIGN_OUT -> sendSignOutRequest()
      NavigationSection.BOOKMARKS -> router.newRootScreen(NavigationScreen.BOOKMARKS_SCREEN)
      NavigationSection.FORUMS -> router.newRootScreen(NavigationScreen.FORUMS_LIST_SCREEN)
      NavigationSection.SETTINGS -> router.navigateTo(NavigationScreen.SETTINGS_SCREEN)
    }
  }

  fun navigateWithIntentLink(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
  }

  private fun navigateToDefaultMainPage() {
    when (settings.getStartingPage()) {
      DefaultHomeScreen.FORUMS -> router.newRootScreen(NavigationScreen.FORUMS_LIST_SCREEN)
      DefaultHomeScreen.ACTIVE_TOPICS -> router.newRootScreen(NavigationScreen.ACTIVE_TOPICS_SCREEN)
      DefaultHomeScreen.INCUBATOR -> router.newRootScreen(NavigationScreen.INCUBATOR_SCREEN)
      else -> router.newRootScreen(NavigationScreen.NEWS_SCREEN)
    }
  }

  private fun refreshAuthorization() {
    getSessionInfoUseCase
        .buildUseCaseObservable(Unit)
        .subscribeOn(Schedulers.io())
        .map { sessionInfo: BaseEntity -> sessionInfoMapper.transform(sessionInfo) }
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getLoginSessionInfoObserver())
  }

  private fun getLoginSessionInfoObserver() =
      object : DisposableObserver<YapEntity>() {

        override fun onNext(info: YapEntity) {
          displayLoginSessionInfo(info)
        }

        override fun onComplete() {
          Timber.i("Login session info updated.")
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }

  private fun displayLoginSessionInfo(sessionInfo: YapEntity) {

    sessionInfo as LoginSessionInfoModel

    currentUserKey = sessionInfo.sessionId
    viewState.updateNavDrawerProfile(sessionInfo)
    viewState.clearDynamicNavigationItems()

    if (sessionInfo.nickname.isEmpty()) {
      viewState.displaySignedOutNavigation()
    } else {
      viewState.displaySignedInNavigation()
    }
  }

  private fun sendSignOutRequest() {
    signOutUseCase
        .buildUseCaseObservable(Params(userKey = currentUserKey))
        .subscribeOn(Schedulers.io())
        .map { response: BaseEntity -> serverResponseMapper.transform(response) }
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe(getSignOutResponseObserver())
  }

  private fun getSignOutResponseObserver() =
      object : DisposableObserver<YapEntity>() {

        override fun onNext(response: YapEntity) {
          checkIfSignedOutSuccessfully(response)
        }

        override fun onComplete() {
          Timber.i("Sign Out request completed.")
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }

  private fun checkIfSignedOutSuccessfully(serverResponse: YapEntity) {
    serverResponse as ServerResponseModel

    if (serverResponse.text.contains(SIGN_OUT_SUCCESS_MARKER)) {
      viewState.showSignOutMessage()
      refreshAuthorization()
      navigateToDefaultMainPage()
    }
  }
}
