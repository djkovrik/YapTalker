package com.sedsoftware.yaptalker.features.navigation

import com.arellomobile.mvp.InjectViewState
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.sedsoftware.yaptalker.base.navigation.RequestCodes
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class NavigationDrawerPresenter : BasePresenter<NavigationDrawerView>() {

  init {
    router.setResultListener(RequestCodes.SIGN_IN, {
      refreshAuthorization()
      goToDefaultMainPage()
    })
  }

  private val cookieStorage: ClearableCookieJar by instance()

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    goToDefaultMainPage()
  }

  override fun attachView(view: NavigationDrawerView?) {
    super.attachView(view)
    refreshAuthorization()
  }

  override fun onDestroy() {
    super.onDestroy()
    router.removeResultListener(RequestCodes.SIGN_IN)
  }

  fun onNavigationDrawerClicked(@NavigationDrawerItems.Section identifier: Long) {
    when (identifier) {
      NavigationDrawerItems.MAIN_PAGE -> router.newRootScreen(NavigationScreens.NEWS_SCREEN)
      NavigationDrawerItems.FORUMS -> router.newRootScreen(NavigationScreens.FORUMS_LIST_SCREEN)
      NavigationDrawerItems.ACTIVE_TOPICS -> router.newRootScreen(NavigationScreens.ACTIVE_TOPICS_SCREEN)
      NavigationDrawerItems.BOOKMARKS -> router.newRootScreen(NavigationScreens.BOOKMARKS_SCREEN)
      NavigationDrawerItems.SETTINGS -> router.navigateTo(NavigationScreens.SETTINGS_SCREEN)
      NavigationDrawerItems.SIGN_IN -> router.navigateTo(NavigationScreens.AUTHORIZATION_SCREEN)
      NavigationDrawerItems.SIGN_OUT -> signOut()
    }
  }

  private fun refreshAuthorization() {

    yapDataManager
        .getAuthorizedUserInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          userInfo ->
          viewState.updateNavDrawerProfile(userInfo)
          viewState.clearDynamicNavigationItems()

          if (userInfo.nickname.isEmpty()) {
            viewState.displaySignedOutNavigation()
          } else {
            viewState.displaySignedInNavigation()
          }
        }, {
          // onError
          throwable ->
          throwable.message?.let { viewState.showErrorMessage(it) }
        })
  }

  private fun goToDefaultMainPage() {
    router.newRootScreen(settings.getStartingPage())
  }

  // TODO() Add sign out request
  private fun signOut() {
    cookieStorage.clear()
    viewState.showSignOutMessage()
    refreshAuthorization()
    goToDefaultMainPage()
  }
}
