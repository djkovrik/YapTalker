package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test

class NavigationViewPresenterTest {

  val navigationView = mock<NavigationView>()
  val navigationViewState = mock<`NavigationView$$State`>()

  lateinit var savedInstanceState: Bundle
  lateinit var presenter: NavigationViewPresenter

  @Before
  fun setUp() {
    savedInstanceState = Bundle()
    presenter = NavigationViewPresenter()
    presenter.attachView(navigationView)
    presenter.setViewState(navigationViewState)
  }

  @Test
  fun navigation_shouldInitDrawerAndRouter() {
    presenter.initLayout(savedInstanceState)
    verify(navigationViewState).initDrawer(savedInstanceState)
  }

  @Test
  fun navigation_clickShouldLaunchChosenSectionTransition() {
    presenter.onNavigationClicked(1L)
    verify(navigationViewState).goToChosenSection(1L)
  }
}