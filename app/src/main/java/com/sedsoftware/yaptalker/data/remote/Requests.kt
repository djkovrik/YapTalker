package com.sedsoftware.yaptalker.data.remote

import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.Forums
import com.sedsoftware.yaptalker.data.model.News
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.model.video.CoubData
import com.sedsoftware.yaptalker.data.model.video.RutubeData
import io.reactivex.Single
import retrofit2.http.GET
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
