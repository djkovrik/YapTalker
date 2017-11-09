package com.sedsoftware.yaptalker.features.forum

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.BaseTestClassForPresenters
import com.sedsoftware.yaptalker.TestKodeinInstanceRule
import com.sedsoftware.yaptalker.callProtectedPresenterMethod
import com.sedsoftware.yaptalker.getDummyForumPage
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

class ChosenForumPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val chosenForumView = mock<ChosenForumView>()
  private val chosenForumViewState = mock<`ChosenForumView$$State`>()
  private lateinit var presenter: ChosenForumPresenter

  private val forumPage = getDummyForumPage()

  @Before
  fun setUp() {
    whenever(yapDataManagerMock
        .getChosenForum(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
        .thenReturn(Single.just(forumPage))

    presenter = ChosenForumPresenter()
    presenter.attachView(chosenForumView)
    presenter.setViewState(chosenForumViewState)
  }

  @Test
  fun onFirstViewAttachInitiatesForumPageUpdate() {
    callProtectedPresenterMethod(presenter, "onFirstViewAttach")
    verify(chosenForumViewState).initiateForumLoading()
  }

  @Test
  fun normalViewAttachesDoNotInitiateForumPageUpdate() {
    presenter.detachView(chosenForumView)
    presenter.attachView(chosenForumView)
    verify(chosenForumViewState, never()).initiateForumLoading()
    presenter.detachView(chosenForumView)
    presenter.attachView(chosenForumView)
    verify(chosenForumViewState, never()).initiateForumLoading()
  }

  @Test
  fun incorrectPageRequestShowsErrorMessage() {
    presenter.goToChosenPage(1000)
    verify(chosenForumViewState).showCantLoadPageMessage(1000)
  }
}
