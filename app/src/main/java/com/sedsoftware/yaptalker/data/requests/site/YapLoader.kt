package com.sedsoftware.yaptalker.data.requests.site

import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsPage
import com.sedsoftware.yaptalker.data.parsing.AuthorizedUserInfo
import com.sedsoftware.yaptalker.data.parsing.Bookmarks
import com.sedsoftware.yaptalker.data.parsing.ForumPage
import com.sedsoftware.yaptalker.data.parsing.Forums
import com.sedsoftware.yaptalker.data.parsing.News
import com.sedsoftware.yaptalker.data.parsing.TopicPage
import com.sedsoftware.yaptalker.data.parsing.UserProfile
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

interface YapLoader {

  @GET("/st/{startPage}/")
  fun loadNews(@Path("startPage") startPage: Int): Single<News>

  @GET("/forum")
  fun loadForumsList(): Single<Forums>

  @GET("/forum{forumId}/st/{startFrom}/100/Z-A/{sortingMode}")
  fun loadForumPage(
      @Path("forumId") forumId: Int,
      @Path("startFrom") startFrom: Int,
      @Path("sortingMode") sortingMode: String): Single<ForumPage>

  @GET("/forum{forumId}/st/{startFrom}/topic{topicId}.html")
  fun loadTopicPage(
      @Path("forumId") forumId: Int,
      @Path("topicId") topicId: Int,
      @Path("startFrom") startPostNumber: Int): Single<TopicPage>

  @GET("/members/member{profileId}.html")
  fun loadUserProfile(
      @Path("profileId") profileId: Int): Single<UserProfile>

  @GET("/forum")
  fun loadAuthorizedUserInfo(): Single<AuthorizedUserInfo>

  @FormUrlEncoded
  @POST("/act/Login/CODE/01/")
  fun signIn(
      @Field("CookieDate") cookieDate: String,
      @Field("PassWord") password: String,
      @Field("UserName") userName: String,
      @Field("referer") referer: String,
      @Field("submit") submit: String,
      @Field("user_key") userKey: String): Single<Response<ResponseBody>>

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
      @Part("enabletag") enabletag: Int): Single<TopicPage>

  @GET("/")
  fun loadActiveTopics(
      @Query("act") act: String,
      @Query("CODE") code: String,
      @Query("searchid") searchId: String,
      @Query("st") startTopicNumber: Int): Single<ActiveTopicsPage>

  @Headers("X-Requested-With:XMLHttpRequest")
  @GET("/")
  fun loadBookmarks(
      @Query("act") act: String,
      @Query("CODE") code: String): Single<Bookmarks>

  @Headers("X-Requested-With:XMLHttpRequest")
  @GET("/")
  fun addToBookmarks(
      @Query("act") act: String,
      @Query("CODE") code: String,
      @Query("item") item: Int,
      @Query("st") startPostNumber: Int,
      @Query("type") type: Int): Single<Response<ResponseBody>>

  @Headers("X-Requested-With:XMLHttpRequest")
  @GET("/")
  fun removeFromBookmarks(
      @Query("act") act: String,
      @Query("CODE") code: String,
      @Query("id") id: Int): Single<Response<ResponseBody>>
}
