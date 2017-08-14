package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.features.base.BasePresenter

@InjectViewState
class NavigationViewPresenter : BasePresenter<NavigationView>() {

  fun initLayout(savedInstanceState: Bundle?) {
    viewState.initDrawer(savedInstanceState)
  }

  fun onNavigationClicked(@Navigation.Section section: Long) {
    viewState.goToChosenSection(section)
  }
}
