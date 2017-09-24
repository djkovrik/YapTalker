package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.commons.enums.PresenterLifecycle
import com.sedsoftware.yaptalker.features.base.BasePresenter
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@InjectViewState
class NavigationViewPresenter : BasePresenter<NavigationView>() {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    titleChannel
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { text -> viewState.setAppbarTitle(text) }
  }

  fun initLayout(savedInstanceState: Bundle?) {
    viewState.initDrawer(savedInstanceState)
  }

  fun onNavigationClicked(@Navigation.Section section: Long) {
    viewState.goToChosenSection(section)
  }

  fun getFirstLaunchPage() = settings.getStartingPage()

  fun loginAttempt() {
    yapDataManager
        .loginToSite(login = "test", password = "test")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // On Susccess
          response ->
          Timber.d("GOT RESPONSE: ${response.body()?.string()}")
        }, {
          // On Error
          error ->
          Timber.d("GOT ERROR: ${error.message}")
        })
  }
}
