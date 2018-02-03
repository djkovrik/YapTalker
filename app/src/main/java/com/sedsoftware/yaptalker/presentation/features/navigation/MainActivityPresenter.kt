package com.sedsoftware.yaptalker.presentation.features.navigation

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.events.AppEvent.AppbarEvent
import com.sedsoftware.yaptalker.presentation.base.events.AppEvent.NavDrawerEvent
import com.uber.autodispose.kotlin.autoDisposable
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MainActivityPresenter @Inject constructor() : BasePresenter<MainActivityView>() {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    eventBus
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ event ->
        when (event) {
          is AppbarEvent -> viewState.setAppbarTitle(event.title)
          is NavDrawerEvent -> viewState.selectNavDrawerItem(event.itemId)
        }
      }, { throwable ->
        Timber.e("Error while handling app event: ${throwable.message}")
      })
  }
}
