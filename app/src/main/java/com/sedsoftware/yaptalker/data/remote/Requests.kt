package com.sedsoftware.yaptalker.data.remote

import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.Forums
import com.sedsoftware.yaptalker.data.model.News
import com.sedsoftware.yaptalker.data.model.TopicPage
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
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface YapLoader {

  /**
   * Load news from main page
   *
   * @param startPage Starting page number, also defines how much pages will be loaded.
   */
  @GET("/st/{startPage}/")
  fun loadNews(@Path("startPage") startPage: Int): Single<News>

  /**
   * Load main forums list
   */
  @GET("/forum")
  fun loadForumsList(): Single<Forums>

  /**
   * Load chosen forum page.
   *
   * @param forumId Chosen forum id
   * @param startFrom Starting page (first page equals 0, should be multiply of 30)
   * @param sortingMode Possible values: last_post and rank
   */
  @GET("/forum{forumId}/st/{startFrom}/100/Z-A/{sortingMode}")
  fun loadForumPage(
      @Path("forumId") forumId: Int,
      @Path("startFrom") startFrom: Int,
      @Path("sortingMode") sortingMode: String): Single<ForumPage>

  /**
   * Load chosen topic.
   *
   * @param forumId Parent forum id
   * @param topicId Chosen topic id
   * @param startPostNumber Starting page (first page equals 0, should be multiply of 25)
   */
  @GET("/forum{forumId}/st/{startFrom}/topic{topicId}.html")
  fun loadTopicPage(
      @Path("forumId") forumId: Int,
      @Path("topicId") topicId: Int,
      @Path("startFrom") startPostNumber: Int): Single<TopicPage>

  /**
   * Site login request.
   *
   * @param cookieDate Should be equal to 1 for persistent cookies.
   * @param password User password.
   * @param userName User login.
   * @param referer Site referrer.
   * @param submit Submit action, should be equal to "Вход".
   * @param user_key Unique user hash.
   */
  @FormUrlEncoded
  @POST("/act/Login/CODE/01/")
  fun signIn(
      @Field("CookieDate") cookieDate: String,
      @Field("PassWord") password: String,
      @Field("UserName") userName: String,
      @Field("referer") referer: String,
      @Field("submit") submit: String,
      @Field("user_key") userKey: String): Single<Response<ResponseBody>>
}

interface RutubeLoader {
  /**
   * Loads thumbnail for Rutube video.
   *
   * @param id Target video id.
   */
  @GET("/api/video/{id}")
  fun loadThumbnail(@Path("id") id: String, @Query("format") format: String): Single<RutubeData>
}

interface CoubLoader {
  /**
   * Loads thumbnail for Coub.
   *
   * @param url Target coub url.
   */
  @GET("/api/oembed.json")
  fun loadThumbnail(@Query("url") url: String): Single<CoubData>
}

interface YapVideoLoader {
  /**
   * Loads thumbnail for new Yap video player.
   *
   * @param id Target video id.
   * @param md5 Video player hash.
   * @param type Response type
   */
  @GET("/load/{id}")
  fun loadThumbnail(
      @Path("id") id: String,
      @Query("md5") md5: String,
      @Query("type") type: String): Single<YapVideoData>
}

interface VkLoader {
  /**
   * Loads thumbnail for vk video.
   *
   * @param videos Target video id.
   * @param access_token Application access token.
   * @param version API version.
   */
  @GET("/method/video.get")
  fun loadThumbnail(
      @Query("videos") videos: String,
      @Query("access_token") access_token: String,
      @Query("v") version: String): Single<VkResponseWrapper>
}
