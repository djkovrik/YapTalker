package com.sedsoftware.yaptalker.features.navigation

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.BaseTestClassForPresenters
import com.sedsoftware.yaptalker.TestKodeinInstanceRule
import com.sedsoftware.yaptalker.callProtectedPresenterMethod
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val navigationView = mock<NavigationView>()
  private val navigationViewState = mock<`NavigationView$$State`>()
  private lateinit var presenter: NavigationPresenter

  @Before
  fun setUp() {
    presenter = NavigationPresenter()
    presenter.attachView(navigationView)
    presenter.setViewState(navigationViewState)
  }

  @Test
  fun presenterShowsEulaOnFirstViewAttach() {
    whenever(settingsMock.isEulaAccepted()).thenReturn(false)
    callProtectedPresenterMethod(presenter, "onFirstViewAttach")
    verify(navigationViewState).showEula()
  }

  @Test
  fun presenterDoesNotShowEulaWhenConfirmed() {
    whenever(settingsMock.isEulaAccepted()).thenReturn(true)
    callProtectedPresenterMethod(presenter, "onFirstViewAttach")
    verify(navigationViewState, never()).showEula()
  }
}
