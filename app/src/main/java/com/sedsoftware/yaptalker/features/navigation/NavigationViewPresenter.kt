package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.commons.enums.PresenterLifecycle
import com.sedsoftware.yaptalker.features.base.BasePresenter
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@InjectViewState
class NavigationViewPresenter : BasePresenter<NavigationView>() {

  private val cookieStorage: ClearableCookieJar by instance()

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    titleChannel
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { text -> viewState.setAppbarTitle(text) }

    authorizationChannel
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { info -> viewState.setActiveProfile(info) }
  }

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
          pushAuthorizationStatus(authorizationChannel, info.getUserInfo())
        }, {
          // On Error
          t ->
          Timber.d("Can't get authorization status! Error: ${t.message}")
        })
  }

  fun signOut() {
    cookieStorage.clear()
    viewState.showSignOutMessage()
  }
}
