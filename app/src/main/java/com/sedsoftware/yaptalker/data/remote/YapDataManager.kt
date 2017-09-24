package com.sedsoftware.yaptalker.data.remote

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.commons.enums.YapRequestState
import com.sedsoftware.yaptalker.commons.extensions.toMD5
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.model.createForumsList
import com.sedsoftware.yaptalker.data.model.createNewsList
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.ResponseBody
import retrofit2.Response

class YapDataManager(private val yapLoader: YapLoader, val requestState: BehaviorRelay<Long>) {

  companion object {
    private const val LOGIN_REFERER = "http://www.yaplakal.com/forum/"
    private const val LOGIN_SUBMIT = "Вход"
  }

  fun getNews(startNumber: Int = 0): Observable<NewsItem> =
      yapLoader
          .loadNews(startNumber)
          .map { news -> news.createNewsList() }
          .flatMapObservable { list -> Observable.fromIterable(list) }
          .doOnSubscribe {
            publishRequestState(YapRequestState.LOADING)
          }
          .doOnError {
            publishRequestState(YapRequestState.ERROR)
          }
          .doOnComplete {
            publishRequestState(YapRequestState.COMPLETED)
          }

  fun getForumsList(): Observable<ForumItem> =
      yapLoader
          .loadForumsList()
          .map { forums -> forums.createForumsList() }
          .flatMapObservable { list -> Observable.fromIterable(list) }
          .doOnSubscribe {
            publishRequestState(YapRequestState.LOADING)
          }
          .doOnError {
            publishRequestState(YapRequestState.ERROR)
          }
          .doOnComplete {
            publishRequestState(YapRequestState.COMPLETED)
          }

  fun getChosenForum(forumId: Int, startNumber: Int, sortingMode: String): Single<ForumPage> =
      yapLoader
          .loadForumPage(forumId, startNumber, sortingMode)
          .doOnSubscribe {
            publishRequestState(YapRequestState.LOADING)
          }
          .doOnError {
            publishRequestState(YapRequestState.ERROR)
          }
          .doOnSuccess {
            publishRequestState(YapRequestState.COMPLETED)
          }

  fun getChosenTopic(forumId: Int, topicId: Int, startPostNumber: Int): Single<TopicPage> =
      yapLoader
          .loadTopicPage(forumId, topicId, startPostNumber)
          .doOnSubscribe {
            publishRequestState(YapRequestState.LOADING)
          }
          .doOnError {
            publishRequestState(YapRequestState.ERROR)
          }
          .doOnSuccess {
            publishRequestState(YapRequestState.COMPLETED)
          }

  fun loginToSite(login: String, password: String): Single<Response<ResponseBody>> {

    return Single
        .just(login)
        .map { str -> "$str${System.currentTimeMillis()}".toMD5() }
        .flatMap { hash ->
          yapLoader
              .signIn(
                  cookieDate = "1",
                  password = password,
                  userName = login,
                  referer = LOGIN_REFERER,
                  submit = LOGIN_SUBMIT,
                  userKey = hash)
        }
  }

  private fun publishRequestState(@YapRequestState.State currentState: Long) {
    Observable.just(currentState)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(requestState)
  }
}
