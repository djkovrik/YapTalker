package com.sedsoftware.yaptalker.data.remote

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.data.remote.yap.YapNewsLoader
import com.sedsoftware.yaptalker.getDummyNewsList
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

class YapNewsLoaderTest {

  var loaderMock = mock<YapNewsLoader>()
  var testSubscriber = TestObserver<List<NewsItem>>()

  @Before
  fun setUp() {
    testSubscriber = TestObserver<List<NewsItem>>()
    loaderMock = mock<YapNewsLoader>()

    whenever(loaderMock.loadNews(ArgumentMatchers.anyInt()))
        .thenReturn(Single.just(getDummyNewsList()))
  }

  @Test
  fun testYapNewsLoaderSubscription() {

    // Action
    loaderMock.loadNews(123).subscribe(testSubscriber)

    // Assert
    testSubscriber.assertNoErrors()
    testSubscriber.assertValueCount(1)
    testSubscriber.assertValue({ list: List<NewsItem> -> list.size == 5 })
    testSubscriber.assertComplete()
  }
}