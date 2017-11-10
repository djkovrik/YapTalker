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

class MainActivityPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val mainActivityView = mock<MainActivityView>()
  private val mainActivityViewState = mock<`MainActivityView$$State`>()
  private lateinit var presenter: MainActivityPresenter

  @Before
  fun setUp() {
    presenter = MainActivityPresenter()
    presenter.attachView(mainActivityView)
    presenter.setViewState(mainActivityViewState)
  }

  @Test
  fun onFirstViewAttachShowsEula() {
    whenever(settingsMock.isEulaAccepted()).thenReturn(false)
    callProtectedPresenterMethod(presenter, "onFirstViewAttach")
    verify(mainActivityViewState).showEula()
  }

  @Test
  fun onFirstViewAttachDoesNotShowEulaWhenConfirmed() {
    whenever(settingsMock.isEulaAccepted()).thenReturn(true)
    callProtectedPresenterMethod(presenter, "onFirstViewAttach")
    verify(mainActivityViewState, never()).showEula()
  }
}
