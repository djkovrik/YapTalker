package com.sedsoftware.yaptalker.data.remote

import com.sedsoftware.yaptalker.data.model.AuthorizedUserInfo
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.Forums
import com.sedsoftware.yaptalker.data.model.News
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.model.UserProfile
import com.sedsoftware.yaptalker.data.remote.video.CoubData
import com.sedsoftware.yaptalker.data.remote.video.RutubeData
import com.sedsoftware.yaptalker.data.remote.video.VkResponseWrapper
import com.sedsoftware.yaptalker.data.remote.video.YapVideoData
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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
      @Part("enabletag") enabletag: Int): Single<Response<ResponseBody>>
}

interface RutubeLoader {
  @GET("/api/video/{id}")
  fun loadThumbnail(@Path("id") id: String, @Query("format") format: String): Single<RutubeData>
}

interface CoubLoader {
  @GET("/api/oembed.json")
  fun loadThumbnail(@Query("url") url: String): Single<CoubData>
}

interface YapFileLoader {
  @GET("/get_player")
  fun loadHash(
      @Query("v") v: String): Single<String>
}

interface YapVideoLoader {
  @GET("/load/{id}")
  fun loadThumbnail(
      @Path("id") id: String,
      @Query("md5") md5: String,
      @Query("type") type: String): Single<YapVideoData>
}

interface VkLoader {
  @GET("/method/video.get")
  fun loadThumbnail(
      @Query("videos") videos: String,
      @Query("access_token") access_token: String,
      @Query("v") version: String): Single<VkResponseWrapper>
}
