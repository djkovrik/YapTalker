package com.sedsoftware.yaptalker.data.remote

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.data.model.News
import com.sedsoftware.yaptalker.data.remote.yap.YapLoader
import com.sedsoftware.yaptalker.getDummyNews
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

class YapNewsLoaderTest {

  var loaderMock = mock<YapLoader>()
  var testSubscriber = TestObserver<News>()

  @Before
  fun setUp() {
    testSubscriber = TestObserver<News>()
    loaderMock = mock<YapLoader>()

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