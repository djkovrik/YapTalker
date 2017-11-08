package com.sedsoftware.yaptalker.features.news

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.BaseTestClassForPresenters
import com.sedsoftware.yaptalker.TestKodeinInstanceRule
import com.sedsoftware.yaptalker.getDummyNewsItem
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

class NewsPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val newsView = mock<NewsView>()
  private val newsViewState = mock<`NewsView$$State`>()
  private lateinit var presenter: NewsPresenter

  private val dummyNewsItem = getDummyNewsItem()
  private val SCROLL_DOWN_DIFF = 1
  private val SCROLL_UP_DIFF = -1

  @Before
  fun setUp() {
    whenever(yapDataManagerMock.getNews(ArgumentMatchers.anyInt()))
        .thenReturn(Observable.just(dummyNewsItem))

    presenter = NewsPresenter()
    presenter.attachView(newsView)
    presenter.setViewState(newsViewState)
  }

  @Test
  fun presenterHidesFabOnScrollDown() {
    presenter.onScrollFabVisibility(SCROLL_DOWN_DIFF)
    verify(newsViewState).hideFab()
  }

  @Test
  fun presenterShowsFabOnScrollUp() {
    presenter.onScrollFabVisibility(SCROLL_UP_DIFF)
    verify(newsViewState).showFab()
  }
}
