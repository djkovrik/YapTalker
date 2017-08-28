package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.features.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class NavigationViewPresenter : BasePresenter<NavigationView>() {

  init {
    YapTalkerApp.appComponent.inject(this)
  }

  @Inject
  lateinit var titleChannel: BehaviorRelay<String>

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    val subscription = titleChannel
        .subscribe { text -> viewState.setAppbarTitle(text) }

    unsubscribeOnDestroy(subscription)
  }

  fun initLayout(savedInstanceState: Bundle?) {
    viewState.initDrawer(savedInstanceState)
  }

  fun onNavigationClicked(@Navigation.Section section: Long) {
    viewState.goToChosenSection(section)
  }
}
