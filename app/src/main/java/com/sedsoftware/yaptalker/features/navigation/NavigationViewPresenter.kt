package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class NavigationViewPresenter : BasePresenter<NavigationView>() {

  private val cookieStorage: ClearableCookieJar by instance()

  fun initLayout(savedInstanceState: Bundle?) {
    viewState.initDrawer(savedInstanceState)
  }

  fun onNavigationClicked(@Navigation.Section section: Long) {
    viewState.goToChosenSection(section)
  }

  fun getFirstLaunchPage() = settings.getStartingPage()

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

  fun signOut() {
    cookieStorage.clear()
    viewState.showSignOutMessage()
    viewState.goToMainPage()
  }
}
