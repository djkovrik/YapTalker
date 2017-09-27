package com.sedsoftware.yaptalker.data.remote

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.commons.AppEvent
import com.sedsoftware.yaptalker.commons.RequestStateEvent
import com.sedsoftware.yaptalker.commons.extensions.toMD5
import com.sedsoftware.yaptalker.data.model.AuthorizedUserInfo
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

class YapDataManager(
    private val yapLoader: YapLoader,
    private val eventBus: BehaviorRelay<AppEvent>) {

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
            publishConnectionState(state = true)
          }
          .doOnError {
            publishConnectionState(state = false)
          }
          .doOnComplete {
            publishConnectionState(state = false)
          }

  fun getForumsList(): Observable<ForumItem> =
      yapLoader
          .loadForumsList()
          .map { forums -> forums.createForumsList() }
          .flatMapObservable { list -> Observable.fromIterable(list) }
          .doOnSubscribe {
            publishConnectionState(state = true)
          }
          .doOnError {
            publishConnectionState(state = false)
          }
          .doOnComplete {
            publishConnectionState(state = false)
          }

  fun getChosenForum(forumId: Int, startNumber: Int, sortingMode: String): Single<ForumPage> =
      yapLoader
          .loadForumPage(forumId, startNumber, sortingMode)
          .doOnSubscribe {
            publishConnectionState(state = true)
          }
          .doOnError {
            publishConnectionState(state = false)
          }
          .doOnSuccess {
            publishConnectionState(state = false)
          }

  fun getChosenTopic(forumId: Int, topicId: Int, startPostNumber: Int): Single<TopicPage> =
      yapLoader
          .loadTopicPage(forumId, topicId, startPostNumber)
          .doOnSubscribe {
            publishConnectionState(state = true)
          }
          .doOnError {
            publishConnectionState(state = false)
          }
          .doOnSuccess {
            publishConnectionState(state = false)
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

  fun getAuthorizedUserInfo(): Single<AuthorizedUserInfo> {
    return yapLoader
        .loadAuthorizedUserInfo()
  }

  private fun publishConnectionState(state: Boolean) {
    Observable
        .just(RequestStateEvent(connected = state))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(eventBus)
  }
}
