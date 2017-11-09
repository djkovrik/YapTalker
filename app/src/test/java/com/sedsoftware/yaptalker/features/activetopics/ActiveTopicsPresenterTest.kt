package com.sedsoftware.yaptalker.features.activetopics

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.BaseTestClassForPresenters
import com.sedsoftware.yaptalker.TestKodeinInstanceRule
import com.sedsoftware.yaptalker.getDummyActiveTopicsPage
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

class ActiveTopicsPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val activeTopicsView = mock<ActiveTopicsView>()
  private val activeTopicsViewState = mock<`ActiveTopicsView$$State`>()
  private lateinit var presenter: ActiveTopicsPresenter

  private val activeTopics = getDummyActiveTopicsPage()

  @Before
  fun setUp() {
    whenever(yapDataManagerMock.getSearchId())
        .thenReturn(Single.just(""))

    whenever(yapDataManagerMock.getActiveTopics(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
        .thenReturn(Single.just(activeTopics))

    presenter = ActiveTopicsPresenter()
    presenter.attachView(activeTopicsView)
    presenter.setViewState(activeTopicsViewState)
  }

  @Test
  fun incorrectPageRequestShowsErrorMessage() {
    presenter.goToChosenPage(1000)
    verify(activeTopicsViewState).showCantLoadPageMessage(1000)
  }
}
