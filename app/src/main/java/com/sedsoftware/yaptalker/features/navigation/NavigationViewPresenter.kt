package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.commons.AppEvent
import com.sedsoftware.yaptalker.commons.UpdateAppbarEvent
import com.sedsoftware.yaptalker.commons.UpdateNavDrawerEvent
import com.sedsoftware.yaptalker.commons.extensions.validateURL
import com.sedsoftware.yaptalker.features.base.BasePresenter
import com.sedsoftware.yaptalker.features.base.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@InjectViewState
class NavigationViewPresenter : BasePresenter<NavigationView>() {

  private val cookieStorage: ClearableCookieJar by instance()

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    eventBus
        .filter { event -> event.getType() == AppEvent.UPDATE_APPBAR }
        .map { event -> event as UpdateAppbarEvent }
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { event -> viewState.setAppbarTitle(event.title) }

    eventBus
        .filter { event -> event.getType() == AppEvent.UPDATE_NAVDRAWER }
        .map { event -> event as UpdateNavDrawerEvent }
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { event -> viewState.setActiveProfile(event) }
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
          pushAppEvent(
              UpdateNavDrawerEvent(name = info.nickname, title = info.title, avatar = info.avatar.validateURL()))
        }, {
          // On Error
          t ->
          Timber.d("Can't get user info! Error: ${t.message}")
        })
  }

  fun signOut() {
    cookieStorage.clear()
    viewState.showSignOutMessage()
    viewState.goToMainPage()
  }
}
