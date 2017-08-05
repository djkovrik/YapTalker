package com.sedsoftware.yaptalker.data.remote

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.data.NewsItem
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

class YapDataManager(
    val newsLoader: YapNewsLoader,
    val forumsListLoader: YapForumsListLoader,
    val chosenForumLoader: YapChosenForumLoader,
    val chosenTopicLoader: YapChosenTopicLoader,
    val requestState: BehaviorRelay<Long>) {


  fun publishRequestState(@YapRequestState.State currentState: Long) {
    Observable.just(currentState)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(requestState)
  }

  fun getNews(startNumber: Int = 0): Single<List<NewsItem>> =
      newsLoader
          .loadNews(startNumber)
          .doOnSubscribe { publishRequestState(YapRequestState.LOADING) }
          .doOnError { publishRequestState(YapRequestState.ERROR) }
          .doOnSuccess { publishRequestState(YapRequestState.COMPLETED) }

  fun getForumsList() =
      forumsListLoader
          .loadForumsList()
          .doOnSubscribe { publishRequestState(YapRequestState.LOADING) }
          .doOnError { publishRequestState(YapRequestState.ERROR) }
          .doOnSuccess { publishRequestState(YapRequestState.COMPLETED) }

  fun getChosenForum(forumId: Int, startTopicNumber: Int = 0, sortingMode: String = "last_post") =
      chosenForumLoader
          .loadChosenForum(forumId, startTopicNumber, sortingMode)
          .doOnSubscribe { publishRequestState(YapRequestState.LOADING) }
          .doOnError { publishRequestState(YapRequestState.ERROR) }
          .doOnSuccess { publishRequestState(YapRequestState.COMPLETED) }

  fun getChosenTopic(forumId: Int, startPage: Int = 0, topicId: Int) =
      chosenTopicLoader
          .loadChosenTopic(forumId, startPage, topicId)
          .doOnSubscribe { publishRequestState(YapRequestState.LOADING) }
          .doOnError { publishRequestState(YapRequestState.ERROR) }
          .doOnSuccess { publishRequestState(YapRequestState.COMPLETED) }
}