package com.sedsoftware.yaptalker.features.news

import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.TestComponent
import com.sedsoftware.yaptalker.TestComponentRule
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.di.ApplicationComponent
import com.sedsoftware.yaptalker.getDummyNewsList
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

class NewsPresenterTest {

  companion object {
    val news = getDummyNewsList()
    val error = RuntimeException("Test error")
  }

  @Rule
  @JvmField
  val testComponentRule: TestComponentRule = TestComponentRule(testAppComponent())

  val dataManagerMock = mock<YapDataManager>()
  val newsViewState = mock<`NewsView$$State`>()

  lateinit var presenter: NewsPresenter

  @Before
  fun setUp() {

    presenter = NewsPresenter()
    presenter.setViewState(newsViewState)

    RxAndroidPlugins.reset()
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
  }

  @Test
  fun news_presenterShouldCallRefreshNewsAfterForcedReload() {

    whenever(dataManagerMock.getNews(ArgumentMatchers.anyInt()))
        .thenReturn(Single.just(news))

    presenter.loadNews(true)
    presenter.onLoadingSuccess(news)
    verify(newsViewState, atLeastOnce()).refreshNews(news)
  }

  @Test
  fun news_presenterShouldCallAppendNewsIfReloadNotForced() {

    whenever(dataManagerMock.getNews(ArgumentMatchers.anyInt()))
        .thenReturn(Single.just(news))

    presenter.loadNews(false)
    presenter.onLoadingSuccess(news)
    verify(newsViewState, atLeastOnce()).appendNews(news)
  }

  @Test
  fun news_presenterShouldShowErrorMessageOnError() {

    presenter.onLoadingError(error)
    verify(newsViewState).showErrorMessage(error.message!!)
  }

  fun testAppComponent(): ApplicationComponent {
    return object : TestComponent() {
      override fun inject(presenter: NewsPresenter) {
        presenter.yapDataManager = dataManagerMock
      }
    }
  }
}