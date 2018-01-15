package com.sedsoftware.yaptalker.data.network.site

import com.sedsoftware.yaptalker.data.parsed.ActiveTopicsPageParsed
import com.sedsoftware.yaptalker.data.parsed.BookmarksParsed
import com.sedsoftware.yaptalker.data.parsed.EditedPostParsed
import com.sedsoftware.yaptalker.data.parsed.ForumPageParsed
import com.sedsoftware.yaptalker.data.parsed.ForumsListParsed
import com.sedsoftware.yaptalker.data.parsed.LoginSessionInfoParsed
import com.sedsoftware.yaptalker.data.parsed.NewsPageParsed
import com.sedsoftware.yaptalker.data.parsed.QuotedPostParsed
import com.sedsoftware.yaptalker.data.parsed.SitePreferencesPageParsed
import com.sedsoftware.yaptalker.data.parsed.TopicPageParsed
import com.sedsoftware.yaptalker.data.parsed.UserProfileParsed
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit 2 interface definition for sending and retrieving data from the site.
 */
interface YapLoader {

  /**
   * Load news from the main page.
   *
   * @param startPage Chosen page, starts from zero.
   *
   * @return Parsed news page Observable.
   */
  @GET("/st/{startPage}/")
  fun loadNews(@Path("startPage") startPage: Int): Observable<NewsPageParsed>


  /**
   * Load forums list from the site.
   *
   * @return Parsed forums list page Observable.
   */
  @GET("/forum")
  fun loadForumsList(): Observable<ForumsListParsed>


  /**
   * Load chosen forum page from the site.
   *
   * @param forumId Chosen forum id.
   * @param startFrom Starting page number.
   * @param sortingMode Sorting mode (rank or last_post).
   *
   * @return Parsed forum page Observable.
   */
  @GET("/forum{forumId}/st/{startFrom}/100/Z-A/{sortingMode}")
  fun loadForumPage(
      @Path("forumId") forumId: Int,
      @Path("startFrom") startFrom: Int,
      @Path("sortingMode") sortingMode: String
  ): Observable<ForumPageParsed>


  /**
   * Load chosen topic page from the site.
   *
   * @param forumId Topic's parent forum id.
   * @param topicId Chosen topic id.
   * @param startPostNumber Starting page number.
   *
   * @return Parsed topic page Single.
   */
  @GET("/forum{forumId}/st/{startFrom}/topic{topicId}.html")
  fun loadTopicPage(
      @Path("forumId") forumId: Int,
      @Path("topicId") topicId: Int,
      @Path("startFrom") startPostNumber: Int
  ): Single<TopicPageParsed>


  /**
   * Load user's profile page from the site.
   *
   * @param profileId Chosen user id.
   *
   * @return Parsed user profile page Single.
   */
  @GET("/members/member{profileId}.html")
  fun loadUserProfile(@Path("profileId") profileId: Int): Single<UserProfileParsed>


  /**
   * Load current authorization session info.
   *
   * @return Parsed authorization status summary Single.
   */
  @GET("/forum")
  fun loadAuthorizedUserInfo(): Single<LoginSessionInfoParsed>


  /**
   * Load active topics page.
   *
   * @param act Active topics action type.
   * @param code Active topics action code.
   * @param searchId Current searchId.
   * @param startTopicNumber Starting page number.
   *
   * @return Parsed active topics page Single.
   */
  @GET("/")
  fun loadActiveTopics(
      @Query("act") act: String,
      @Query("CODE") code: String,
      @Query("searchid") searchId: String,
      @Query("st") startTopicNumber: Int
  ): Single<ActiveTopicsPageParsed>


  /**
   * Load bookmarks block.
   *
   * @param act Load bookmarks action type.
   * @param code Load bookmarks action code.
   *
   * @return Parsed bookmarks block Observable.
   */
  @Headers("X-Requested-With:XMLHttpRequest")
  @GET("/")
  fun loadBookmarks(
      @Query("act") act: String,
      @Query("CODE") code: String
  ): Observable<BookmarksParsed>


  /**
   * Load site preferences page.
   *
   * @param act Load user data action type.
   * @param code Code for loading user CP forum settings page.
   *
   * @return Parsed forum settings page Single.
   */
  @GET("/")
  fun loadSitePreferences(
      @Query("act") act: String,
      @Query("CODE") code: String
  ): Single<SitePreferencesPageParsed>


  /**
   * Load targeted post text prepared for quoting.
   *
   * @param forumId Current topic's parent forum id.
   * @param topicId Current topic id.
   * @param targetPostId Quoted post id.
   *
   * @return Parsed quoted text Single.
   */
  @GET("/act/Post/CODE/06/forum{forumId}/topic{topicId}/post/{targetPostId}/st/0/")
  fun loadTargetPostQuotedText(
      @Path("forumId") forumId: Int,
      @Path("topicId") topicId: Int,
      @Path("targetPostId") targetPostId: Int
  ): Single<QuotedPostParsed>


  /**
   * Load target post prepared for editing.
   *
   * @param forumId Current topic's parent forum id.
   * @param topicId Current topic id.
   * @param targetPostId Quoted post id.
   * @param startingPost Current topic page.
   *
   * @return Parsed quoted text Single.
   */
  @GET("/act/Post/CODE/08/forum{forumId}/topic{topicId}/post/{targetPostId}/st/{startingPost}/")
  fun loadTargetPostEditedText(
      @Path("forumId") forumId: Int,
      @Path("topicId") topicId: Int,
      @Path("targetPostId") targetPostId: Int,
      @Path("startingPost") startingPost: Int
  ): Single<EditedPostParsed>


  /**
   * Send message posting request to the site.
   *
   * @param act Message posting action type.
   * @param code Message posting action code.
   * @param forum Topic's parent forum id.
   * @param topic Chosen topic id.
   * @param st Starting page.
   * @param enableemo Enable emoji.
   * @param enablesig Enable signature.
   * @param authKey Authorization key.
   * @param postContent Message content.
   * @param maxFileSize File size limit.
   * @param enabletag Enable tags
   *
   * @return Parsed topic page Single.
   */
  @Suppress("LongParameterList")
  @Multipart
  @POST("/")
  fun postMessage(
      @Part("act") act: String,
      @Part("CODE") code: String,
      @Part("f") forum: Int,
      @Part("t") topic: Int,
      @Part("st") st: Int,
      @Part("enableemo") enableemo: Boolean,
      @Part("enablesig") enablesig: Boolean,
      @Part("auth_key") authKey: String,
      @Part("Post") postContent: String,
      @Part("MAX_FILE_SIZE") maxFileSize: Int,
      @Part("enabletag") enabletag: Int
  ): Single<TopicPageParsed>


  /**
   * Send sign in request to the site.
   *
   * @param cookieDate Cookie behaviour type (set to 1).
   * @param privacy Anonymous sign in (1 or 0).
   * @param password User password.
   * @param userName User login.
   * @param referer Referer link.
   * @param submit Submit action type.
   * @param userKey Generated md5 user hash key.
   *
   * @return Raw site response Single.
   */
  @FormUrlEncoded
  @POST("/act/Login/CODE/01/")
  fun signIn(
      @Field("CookieDate") cookieDate: Int,
      @Field("Privacy") privacy: Boolean,
      @Field("PassWord") password: String,
      @Field("UserName") userName: String,
      @Field("referer") referer: String,
      @Field("submit") submit: String,
      @Field("user_key") userKey: String
  ): Single<Response<ResponseBody>>


  /**
   * Send sign out request to the site.
   *
   * @param key Current user hash key.
   *
   * @return Raw site response Single.
   */
  @GET("/act/Login/CODE/03/")
  fun signOut(@Query("key") key: String): Single<Response<ResponseBody>>


  /**
   * Send adding to bookmarks request.
   *
   * @param act Add to bookmarks action type.
   * @param code Add to bookmarks action code.
   * @param item Chosen topic id.
   * @param startPostNumber Starting page number.
   * @param type Request type (set to 1).
   *
   */
  @Headers("X-Requested-With:XMLHttpRequest")
  @GET("/")
  fun addToBookmarks(
      @Query("act") act: String,
      @Query("CODE") code: String,
      @Query("item") item: Int,
      @Query("st") startPostNumber: Int,
      @Query("type") type: Int
  ): Observable<Response<ResponseBody>>


  /**
   * Send remove to bookmarks request.
   *
   * @param act Add to bookmarks action type.
   * @param code Add to bookmarks action code.
   * @param id Bookmark id.
   *
   * @return Raw site response Single.
   */
  @Headers("X-Requested-With:XMLHttpRequest")
  @GET("/")
  fun removeFromBookmarks(
      @Query("act") act: String,
      @Query("CODE") code: String,
      @Query("id") id: Int
  ): Single<Response<ResponseBody>>


  /**
   * Send karma change request.
   *
   * @param act Add to bookmarks action type.
   * @param code Add to bookmarks action code.
   * @param rank Karma diff (1 or -1).
   * @param postId Target post id.
   * @param topicId Target topic id.
   * @param type Karma type (1 for topic and 0 for post).
   *
   * @return Raw site response Single.
   */
  @Headers("X-Requested-With:XMLHttpRequest")
  @GET("/")
  fun changeKarma(
      @Query("act") act: String,
      @Query("CODE") code: String,
      @Query("rank") rank: Int,
      @Query("p") postId: Int,
      @Query("t") topicId: Int,
      @Query("n") type: Int
  ): Single<Response<ResponseBody>>
}
