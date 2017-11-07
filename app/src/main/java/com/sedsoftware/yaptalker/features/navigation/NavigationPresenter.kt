package com.sedsoftware.yaptalker.features.navigation

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.uber.autodispose.kotlin.autoDisposeWith

@InjectViewState
class NavigationPresenter : BasePresenter<NavigationView>() {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    appbarBus
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe { title ->
          viewState.setAppbarTitle(title)
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
