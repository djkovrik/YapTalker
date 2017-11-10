package com.sedsoftware.yaptalker.features.navigation

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.BaseTestClassForPresenters
import com.sedsoftware.yaptalker.TestKodeinInstanceRule
import com.sedsoftware.yaptalker.callProtectedPresenterMethod
import com.sedsoftware.yaptalker.getDummyUserInfoAuthorized
import com.sedsoftware.yaptalker.getDummyUserInfoNotAuthorized
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val navigationDrawerView = mock<NavigationView>()
  private val navigationDrawerViewState = mock<`NavigationView$$State`>()
  private lateinit var presenter: NavigationPresenter

  private val authorizedUser = getDummyUserInfoAuthorized()
  private val notAuthorizedUser = getDummyUserInfoNotAuthorized()

  @Before
  fun setUp() {
    presenter = NavigationPresenter()
  }

  @Test
  fun refreshAuthorizationUpdatesNavigationDrawerAuthorized() {
    whenever(yapDataManagerMock.getAuthorizedUserInfo())
        .thenReturn(Single.just(authorizedUser))

    presenter.attachView(navigationDrawerView)
    presenter.setViewState(navigationDrawerViewState)

    callProtectedPresenterMethod(presenter, "refreshAuthorization")
    verify(navigationDrawerViewState).updateNavDrawerProfile(authorizedUser)
    verify(navigationDrawerViewState).clearDynamicNavigationItems()
    verify(navigationDrawerViewState).displaySignedInNavigation()
  }

  @Test
  fun refreshAuthorizationUpdatesNavigationDrawerNotAuthorized() {
    whenever(yapDataManagerMock.getAuthorizedUserInfo())
        .thenReturn(Single.just(notAuthorizedUser))

    presenter.attachView(navigationDrawerView)
    presenter.setViewState(navigationDrawerViewState)

    callProtectedPresenterMethod(presenter, "refreshAuthorization")
    verify(navigationDrawerViewState).updateNavDrawerProfile(notAuthorizedUser)
    verify(navigationDrawerViewState).clearDynamicNavigationItems()
    verify(navigationDrawerViewState).displaySignedOutNavigation()
  }
}
