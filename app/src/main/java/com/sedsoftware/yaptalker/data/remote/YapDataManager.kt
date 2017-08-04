package com.sedsoftware.yaptalker.data.remote

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
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

  fun loadNews(startPage: Int = 0) =
      newsLoader
          .loadNews(startPage)
          .doOnSubscribe { publishRequestState(YapRequestState.LOADING) }
          .doOnError { publishRequestState(YapRequestState.ERROR) }
          .doOnSuccess { publishRequestState(YapRequestState.COMPLETED) }

  fun loadForums() =
      forumsListLoader
          .loadForumsList()
          .doOnSubscribe { publishRequestState(YapRequestState.LOADING) }
          .doOnError { publishRequestState(YapRequestState.ERROR) }
          .doOnSuccess { publishRequestState(YapRequestState.COMPLETED) }

  fun loadChosenForum(forumId: Int, startTopicNumber: Int, sortingMode: String = "last_post") =
      chosenForumLoader
          .loadChosenForum(forumId, startTopicNumber, sortingMode)
          .doOnSubscribe { publishRequestState(YapRequestState.LOADING) }
          .doOnError { publishRequestState(YapRequestState.ERROR) }
          .doOnSuccess { publishRequestState(YapRequestState.COMPLETED) }

  fun loadChosenTopic(forumId: Int, startPage: Int = 0, topicId: Int) =
      chosenTopicLoader
          .loadChosenTopic(forumId, startPage, topicId)
          .doOnSubscribe { publishRequestState(YapRequestState.LOADING) }
          .doOnError { publishRequestState(YapRequestState.ERROR) }
          .doOnSuccess { publishRequestState(YapRequestState.COMPLETED) }
}