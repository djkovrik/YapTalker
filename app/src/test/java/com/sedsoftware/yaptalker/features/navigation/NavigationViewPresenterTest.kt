package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NavigationViewPresenterTest {

  @Mock
  lateinit var navigationView: NavigationView

  @Mock
  lateinit var navigationViewState: `NavigationView$$State`

  lateinit var savedInstanceState: Bundle
  lateinit var presenter: NavigationViewPresenter

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
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