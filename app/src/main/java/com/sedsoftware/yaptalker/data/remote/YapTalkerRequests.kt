package com.sedsoftware.yaptalker.data.remote

import com.sedsoftware.yaptalker.data.ForumItem
import com.sedsoftware.yaptalker.data.NewsItem
import com.sedsoftware.yaptalker.data.TopicItem
import com.sedsoftware.yaptalker.data.UserProfileInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface YapNewsLoader {
  @GET("/st/{startPage}/")
  fun loadNews(@Path("startPage") startPage: Int): Observable<List<NewsItem>>
}

interface YapForumsLoader {
  @GET("/forum{forumId}/")
  fun loadForum(@Path("forumId") forumId: Int): Observable<List<ForumItem>>
}

interface YapTopicLoader {
  @GET("/forum{forumId}/st/{startPage}/topic{topicId}.html")
  fun loadTopic(@Path("forumId") forumId: Int,
      @Path("topicId") topicId: Int): Observable<List<TopicItem>>
}

interface YapProfileLoader {
  @GET("/members/member{memberId}.html")
  fun loadUserInfo(@Path("memberId") memberId: Int): Observable<UserProfileInfo>
}