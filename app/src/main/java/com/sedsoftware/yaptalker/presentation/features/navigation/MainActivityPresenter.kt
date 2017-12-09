package com.sedsoftware.yaptalker.presentation.features.navigation

import com.arellomobile.mvp.InjectViewState
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposable
import javax.inject.Inject

@InjectViewState
class MainActivityPresenter @Inject constructor(
    private val appbarRelay: BehaviorRelay<String>,
    private val navDrawerRelay: BehaviorRelay<Long>
) : BasePresenter<MainActivityView>() {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    appbarRelay
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe { title ->
          viewState.setAppbarTitle(title)
        }

    navDrawerRelay
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe { item ->
          viewState.setNavDrawerItem(item)
        }
  }

  fun markEulaAsAccepted() {

  }
}
