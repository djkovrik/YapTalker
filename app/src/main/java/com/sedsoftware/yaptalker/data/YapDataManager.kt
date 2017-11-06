package com.sedsoftware.yaptalker.data

import android.annotation.SuppressLint
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.base.events.ConnectionState
import com.sedsoftware.yaptalker.commons.extensions.toMd5
import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsPage
import com.sedsoftware.yaptalker.data.parsing.AuthorizedUserInfo
import com.sedsoftware.yaptalker.data.parsing.Bookmarks
import com.sedsoftware.yaptalker.data.parsing.ForumItem
import com.sedsoftware.yaptalker.data.parsing.ForumPage
import com.sedsoftware.yaptalker.data.parsing.NewsItem
import com.sedsoftware.yaptalker.data.parsing.TopicPage
import com.sedsoftware.yaptalker.data.parsing.UserProfile
import com.sedsoftware.yaptalker.data.parsing.createForumsList
import com.sedsoftware.yaptalker.data.parsing.createNewsList
import com.sedsoftware.yaptalker.data.requests.site.YapLoader
import com.sedsoftware.yaptalker.data.requests.site.YapSearchIdLoader
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
    // Login
    private const val LOGIN_REFERER = "http://www.yaplakal.com/forum/"
    private const val LOGIN_SUBMIT = "Вход"
    // Message posting
    private const val POST_ACT = "Post"
    private const val POST_CODE = "03"
    private const val POST_MAX_FILE_SIZE = 512000
    // Active topics
    private const val ACTIVE_TOPICS_ACT = "Search"
    private const val ACTIVE_TOPICS_CODE = "getactive"
    // Bookmarks
    private const val BOOKMARKS_ACT = "UserCP"
    private const val BOOKMARKS_CODE_LOAD = "10"
    private const val BOOKMARKS_CODE_ADD = "11"
    private const val BOOKMARKS_CODE_REMOVE = "12"
    // Karma
    private const val KARMA_ACT = "ST"
    private const val KARMA_CODE = "vote_post"
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

  fun getBookmarks(): Single<Bookmarks> =
      yapLoader
          .loadBookmarks(
              act = BOOKMARKS_ACT,
              code = BOOKMARKS_CODE_LOAD)
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
        .map { str -> "$str${System.currentTimeMillis()}".toMd5() }
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

  fun sendMessageToSite(forumId: Int, topicId: Int, page: Int, key: String, message: String): Single<TopicPage> =
      yapLoader
          .postMessage(
              act = POST_ACT,
              code = POST_CODE,
              forum = forumId,
              topic = topicId,
              st = page,
              enableemo = true,
              enablesig = true,
              authKey = key,
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

  fun addTopicToBookmarks(topicId: Int, startPostNumber: Int): Single<Response<ResponseBody>> =
      yapLoader
          .addToBookmarks(
              act = BOOKMARKS_ACT,
              code = BOOKMARKS_CODE_ADD,
              item = topicId,
              startPostNumber = startPostNumber,
              type = 1)

  fun removeTopicFromBookmarks(bookmarkId: Int): Single<Response<ResponseBody>> =
      yapLoader
          .removeFromBookmarks(
              act = BOOKMARKS_ACT,
              code = BOOKMARKS_CODE_REMOVE,
              id = bookmarkId)

  /**
   * Request karma change.
   *
   * @param rank Karma diff, 1 or -1
   * @param postId Target post id
   * @param topicId Target topic id
   * @param type Karma type, 1 for topic and 0 for post.
   */
  fun changeKarma(rank: Int, postId: Int, topicId: Int, type: Int) : Single<Response<ResponseBody>> =
      yapLoader
          .changeKarma(
              act = KARMA_ACT,
              code = KARMA_CODE,
              rank = rank,
              p = postId,
              t = topicId,
              n = type)

  @SuppressLint("RxLeakedSubscription", "RxSubscribeOnError")
  private fun publishConnectionState(@ConnectionState.Event event: Long) {
    Observable
        .just(event)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(connectionRelay)
  }
}
