package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.features.NavigationScreens
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class NavigationViewPresenter : BasePresenter<NavigationView>() {

  private val cookieStorage: ClearableCookieJar by instance()

  private var currentTitle: String = ""

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    appbarBus
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { title ->
          viewState.setAppbarTitle(title)
          currentTitle = title
        }

    goToDefaultMainPage()
  }

  override fun attachView(view: NavigationView?) {
    super.attachView(view)
    router.setResultListener(NavigationActivity.SIGN_IN_REQUEST, { refreshAuthorization() })
  }

  override fun detachView(view: NavigationView?) {
    super.detachView(view)
    router.removeResultListener(NavigationActivity.SIGN_IN_REQUEST)
  }

  fun saveCurrentTitle(key: String, outState: Bundle?) {
    outState?.putString(key, currentTitle)
  }

  fun restoreCurrentTitle(key: String, savedInstanceState: Bundle?) {
    savedInstanceState?.let {
      if (savedInstanceState.containsKey(key)) {
        updateAppbarTitle(savedInstanceState.getString(key))
      }
    }
  }

  fun initLayout(savedInstanceState: Bundle?) {
    viewState.initDrawer(savedInstanceState)
  }

  fun refreshAuthorization() {
    yapDataManager
        .getAuthorizedUserInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // On Success
          info ->
          viewState.updateNavDrawer(info)
        }, {
          // On Error
          throwable ->
          throwable.message?.let { viewState.showErrorMessage(it) }
        })
  }

  fun onNavigationDrawerClicked(@NavigationDrawerItems.Section identifier: Long) {
    when (identifier) {
      NavigationDrawerItems.MAIN_PAGE -> router.navigateTo(NavigationScreens.NEWS_SCREEN)
      NavigationDrawerItems.FORUMS -> router.navigateTo(NavigationScreens.FORUMS_LIST_SCREEN)
      NavigationDrawerItems.SETTINGS -> router.navigateTo(NavigationScreens.SETTINGS_SCREEN)
      NavigationDrawerItems.SIGN_IN -> router.navigateTo(NavigationScreens.AUTHORIZATION_SCREEN)
      NavigationDrawerItems.SIGN_OUT -> signOut()
    }
  }

  private fun signOut() {
    cookieStorage.clear()
    viewState.showSignOutMessage()
    goToDefaultMainPage()
  }

  private fun goToDefaultMainPage() {
    router.newRootScreen(settings.getStartingPage())
  }
}
