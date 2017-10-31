package com.sedsoftware.yaptalker.data.remote

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.base.events.ConnectionState
import com.sedsoftware.yaptalker.commons.extensions.toMD5
import com.sedsoftware.yaptalker.data.model.ActiveTopicsPage
import com.sedsoftware.yaptalker.data.model.AuthorizedUserInfo
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.model.UserProfile
import com.sedsoftware.yaptalker.data.model.createForumsList
import com.sedsoftware.yaptalker.data.model.createNewsList
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.ResponseBody
import retrofit2.Response

class YapDataManager(
    private val yapLoader: YapLoader,
    private val searchIdLoader: YapSearchIdLoader,
    private val connectionRelay: BehaviorRelay<Long>) {

  companion object {
    private const val LOGIN_REFERER = "http://www.yaplakal.com/forum/"
    private const val LOGIN_SUBMIT = "Вход"
    private const val POST_ACT = "Post"
    private const val POST_CODE = "03"
    private const val POST_MAX_FILE_SIZE = 512000

    private const val ACTIVE_TOPICS_ACT = "Search"
    private const val ACTIVE_TOPICS_CODE = "getactive"
  }

  fun getNews(startNumber: Int = 0): Observable<NewsItem> =
      yapLoader
          .loadNews(startNumber)
          .map { news -> news.createNewsList() }
          .flatMapObservable { list -> Observable.fromIterable(list) }
          .doOnSubscribe {
            publishConnectionState(ConnectionState.LOADING)
          }
          .doOnError {
            publishConnectionState(ConnectionState.ERROR)
          }
          .doOnComplete {
            publishConnectionState(ConnectionState.COMPLETED)
          }

  fun getForumsList(): Observable<ForumItem> =
      yapLoader
          .loadForumsList()
          .map { forums -> forums.createForumsList() }
          .flatMapObservable { list -> Observable.fromIterable(list) }
          .doOnSubscribe {
            publishConnectionState(ConnectionState.LOADING)
          }
          .doOnError {
            publishConnectionState(ConnectionState.ERROR)
          }
          .doOnComplete {
            publishConnectionState(ConnectionState.COMPLETED)
          }

  fun getChosenForum(forumId: Int, startNumber: Int, sortingMode: String): Single<ForumPage> =
      yapLoader
          .loadForumPage(forumId, startNumber, sortingMode)
          .doOnSubscribe {
            publishConnectionState(ConnectionState.LOADING)
          }
          .doOnError {
            publishConnectionState(ConnectionState.ERROR)
          }
          .doOnSuccess {
            publishConnectionState(ConnectionState.COMPLETED)
          }

  fun getChosenTopic(forumId: Int, topicId: Int, startPostNumber: Int): Single<TopicPage> =
      yapLoader
          .loadTopicPage(forumId, topicId, startPostNumber)
          .doOnSubscribe {
            publishConnectionState(ConnectionState.LOADING)
          }
          .doOnError {
            publishConnectionState(ConnectionState.ERROR)
          }
          .doOnSuccess {
            publishConnectionState(ConnectionState.COMPLETED)
          }

  fun getUserProfile(profileId: Int): Single<UserProfile> =
      yapLoader
          .loadUserProfile(profileId)
          .doOnSubscribe {
            publishConnectionState(ConnectionState.LOADING)
          }
          .doOnError {
            publishConnectionState(ConnectionState.ERROR)
          }
          .doOnSuccess {
            publishConnectionState(ConnectionState.COMPLETED)
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

  fun sendMessageToSite(forumId: Int, topicId: Int, page: Int, authKey: String, message: String): Single<TopicPage> =
      yapLoader
          .postMessage(
              act = POST_ACT,
              code = POST_CODE,
              forum = forumId,
              topic = topicId,
              st = page,
              enableemo = true,
              enablesig = true,
              authKey = authKey,
              postContent = message,
              maxFileSize = POST_MAX_FILE_SIZE,
              enabletag = 1)


  fun getAuthorizedUserInfo(): Single<AuthorizedUserInfo> {
    return yapLoader
        .loadAuthorizedUserInfo()
  }

  fun getSearchId(): Single<String> =
      searchIdLoader
          .loadSearchIdHash()

  fun getActiveTopics(searchId: String, topicNumber: Int): Single<ActiveTopicsPage> =
      yapLoader
          .loadActiveTopics(ACTIVE_TOPICS_ACT, ACTIVE_TOPICS_CODE, searchId, topicNumber)
          .doOnSubscribe {
            publishConnectionState(ConnectionState.LOADING)
          }
          .doOnError {
            publishConnectionState(ConnectionState.ERROR)
          }
          .doOnSuccess {
            publishConnectionState(ConnectionState.COMPLETED)
          }


  private fun publishConnectionState(@ConnectionState.Event event: Long) {
    Observable
        .just(event)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(connectionRelay)
  }
}
