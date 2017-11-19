package com.sedsoftware.yaptalker.features.navigation

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.uber.autodispose.kotlin.autoDisposeWith

@InjectViewState
class MainActivityPresenter : BasePresenter<MainActivityView>() {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    appbarRelay
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { title ->
          viewState.setAppbarTitle(title)
        }

    navDrawerRelay
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { item ->
          viewState.setNavDrawerItem(item)
        }

    if (!settings.isEulaAccepted()) {
      viewState.showEula()
    }
  }

  fun onEulaAccept() {
    settings.markEulaAccepted()
  }

  fun navigateWithIntentLink(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreens.CHOSEN_TOPIC_SCREEN, triple)
  }
}
