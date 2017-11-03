package com.sedsoftware.yaptalker.data.remote

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.data.parsing.News
import com.sedsoftware.yaptalker.data.requests.site.YapLoader
import com.sedsoftware.yaptalker.getDummyNews
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

class YapNewsLoaderTest {

  private var loaderMock = mock<YapLoader>()
  private var testSubscriber = TestObserver<News>()

  @Before
  fun setUp() {
    testSubscriber = TestObserver()
    loaderMock = mock()

    whenever(loaderMock.loadNews(ArgumentMatchers.anyInt()))
        .thenReturn(Single.just(getDummyNews()))
  }

  @Test
  fun testYapNewsLoaderSubscription() {

    // Action
    loaderMock.loadNews(123).subscribe(testSubscriber)

    // Assert
    testSubscriber.assertNoErrors()
    testSubscriber.assertComplete()
  }
}