@file:Suppress("KDocUnresolvedReference")

package com.sedsoftware.yaptalker.data.remote

import com.sedsoftware.yaptalker.data.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Load news from main page
 *
 * @param startPage Starting page number, also defines how much pages will be loaded.
 */
interface YapNewsLoader {
  @GET("/st/{startPage}/")
  fun loadNews(@Path("startPage") startPage: Int): Observable<List<NewsItem>>
}

/**
 * Load main forums list
 */
interface YapForumsListLoader {
  @GET()
  fun loadForumsList(): Observable<List<ForumItem>>
}

/**
 * Load chosen forum topics list
 *
 * @param forumId Forum id
 * @param startFrom Starting topic number, should be multiple of 30
 * @param sortingMode Topic sorting mode, possible values: last_post and rank
 */
interface YapChosenForumLoader {
  @GET("/forum{forumId}/st/{startFrom}/100/Z-A/{sortingMode}")
  fun loadChosenForum(
      @Path("forumId") forumId: Int,
      @Path("startFrom") startTopicNumber: Int,
      @Path("sortingMode") sortingMode: String): Observable<List<TopicItem>>
}

/**
 * Load chosen topic
 *
 * @param forumId Forum id
 * @param startPage Starting topic page, should be multiple of 25
 * @param topicId Topic id
 */
interface YapTopicLoader {
  @GET("/forum{forumId}/st/{startPage}/topic{topicId}.html")
  fun loadTopic(
      @Path("forumId") forumId: Int,
      @Path("topicId") topicId: Int): Observable<List<PostItem>>
}

/**
 * Load user profile info
 *
 * @param memberId User profile id
 */
interface YapProfileLoader {
  @GET("/members/member{memberId}.html")
  fun loadUserProfile(@Path("memberId") memberId: Int): Observable<UserProfileInfo>
}