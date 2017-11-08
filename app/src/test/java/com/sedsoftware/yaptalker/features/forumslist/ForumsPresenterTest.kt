package com.sedsoftware.yaptalker.features.forumslist

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.BaseTestClassForPresenters
import com.sedsoftware.yaptalker.TestKodeinInstanceRule
import com.sedsoftware.yaptalker.getDummyForumItem
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForumsPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val forumsView = mock<ForumsView>()
  private val forumsViewState = mock<`ForumsView$$State`>()
  private lateinit var presenter: ForumsPresenter

  private val dummyForumItem = getDummyForumItem()

  @Before
  fun setUp() {
    whenever(yapDataManagerMock.getForumsList())
        .thenReturn(Observable.just(dummyForumItem))

    presenter = ForumsPresenter()
    presenter.attachView(forumsView)
    presenter.setViewState(forumsViewState)
  }

  @Test
  fun loadingForumsListClearsPreviousListFirst() {
    presenter.loadForumsList()
    verify(forumsViewState).clearForumsList()
  }
}