package com.sedsoftware.yaptalker.data.remote

import com.nhaarman.mockito_kotlin.mock
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.data.model.TopicItemList
import com.sedsoftware.yaptalker.data.model.UserProfileShort
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`


class YapNewsLoaderTest {

  var loaderMock = mock<YapNewsLoader>()
  var testSubscriber = TestObserver<List<NewsItem>>()

  @Before
  fun setUp() {
    testSubscriber = TestObserver<List<NewsItem>>()
    loaderMock = mock<YapNewsLoader>()

    `when`(loaderMock.loadNews(ArgumentMatchers.anyInt()))
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

  fun getDummyNewsList(): List<NewsItem> {
    return listOf(
        getDummyNewsItem(1),
        getDummyNewsItem(2),
        getDummyNewsItem(3),
        getDummyNewsItem(4),
        getDummyNewsItem(5))
  }

  fun getDummyNewsItem(seed: Int): NewsItem {
    return NewsItem(
        summary = "News summary#$seed",
        forum = "Forum title#$seed",
        topic = TopicItemList(id = seed + 1,
            title = "Title#$seed",
            answers = seed + 2,
            uq = seed + 3,
            author = UserProfileShort(
                id = seed + 4,
                name = "Name#$seed"),
            date = "Date#$seed"))
  }
}