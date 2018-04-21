package com.sedsoftware.yaptalker.presentation.feature.navigation

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.device.settings.DefaultHomeScreen
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.LoginSessionInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.sedsoftware.yaptalker.presentation.mapper.LoginSessionInfoModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class NavigationPresenter @Inject constructor(
  private val router: Router,
  private val settings: Settings,
  private val loginSessionInteractor: LoginSessionInteractor,
  private val sessionInfoMapper: LoginSessionInfoModelMapper
) : BasePresenter<NavigationView>() {

  init {
    router.setResultListener(RequestCode.SIGN_IN, {
      refreshAuthorization()
      navigateToDefaultHomePage()
    })
  }

  private var currentUserKey = ""
  private var currentUserId = 0
  private var isLinkNavigationPending = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    navigateToDefaultHomePage()
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
      NavigationSection.SITE_SEARCH -> router.newRootScreen(NavigationScreen.SEARCH_FORM)
      NavigationSection.FORUMS -> router.newRootScreen(NavigationScreen.FORUMS_LIST_SCREEN)
      NavigationSection.SETTINGS -> router.navigateTo(NavigationScreen.SETTINGS_SCREEN)
      NavigationSection.APP_UPDATES -> router.newRootScreen(NavigationScreen.UPDATES_SCREEN)
    }
  }

  fun navigateWithIntentLink(triple: Triple<Int, Int, Int>) {
    isLinkNavigationPending = true
    router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
  }

  fun navigateToUserProfile() {

    if (currentUserId != 0) {
      viewState.closeNavigationDrawer()
      router.navigateTo(NavigationScreen.USER_PROFILE_SCREEN, currentUserId)
    }
  }

  private fun navigateToDefaultHomePage() {

    if (isLinkNavigationPending) {
      isLinkNavigationPending = false
      return
    }

    when (settings.getStartingPage()) {
      DefaultHomeScreen.FORUMS -> router.newRootScreen(NavigationScreen.FORUMS_LIST_SCREEN)
      DefaultHomeScreen.ACTIVE_TOPICS -> router.newRootScreen(NavigationScreen.ACTIVE_TOPICS_SCREEN)
      DefaultHomeScreen.INCUBATOR -> router.newRootScreen(NavigationScreen.INCUBATOR_SCREEN)
      else -> router.newRootScreen(NavigationScreen.NEWS_SCREEN)
    }
  }

  private fun refreshAuthorization() {
    loginSessionInteractor
      .getLoginSessionInfo()
      .subscribeOn(Schedulers.io())
      .map(sessionInfoMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ info ->
        displayLoginSessionInfo(info)
        Timber.i("Login session info updated.")
      }, { error ->
        Timber.e("Login session refresh error: ${error.message}")
      })
  }

  private fun displayLoginSessionInfo(sessionInfo: YapEntity) {

    sessionInfo as LoginSessionInfoModel

    currentUserKey = sessionInfo.sessionId
    currentUserId = sessionInfo.userId

    viewState.updateNavDrawerProfile(sessionInfo)
    viewState.clearDynamicNavigationItems()

    if (sessionInfo.nickname.isEmpty()) {
      viewState.displaySignedOutNavigation()
    } else {
      viewState.displaySignedInNavigation()
    }
  }

  private fun sendSignOutRequest() {
    loginSessionInteractor
      .sendSignOutRequest(currentUserKey)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({
        refreshAuthorization()
        navigateToDefaultHomePage()
        Timber.i("Sign Out request completed.")
      }, { error ->
        Timber.e("Sign Out error: ${error.message}")
      })
  }
}
