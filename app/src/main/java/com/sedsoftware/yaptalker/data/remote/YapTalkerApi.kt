package com.sedsoftware.yaptalker.data.remote

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface YapTalkerApi {

  companion object {
    val ENDPOINT = "http://www.yaplakal.com/"
  }

  @GET("/st/{startPage}/")
  fun loadNews(@Path("startPage") startPage: Int): Observable<ResponseBody>

  @GET("/forum{forumId}/")
  fun loadForum(@Path("forumId") forumId: Int): Observable<ResponseBody>

  @GET("/forum{forumId}/st/{startPage}/topic{topicId}.html")
  fun loadTopic(@Path("forumId") forumId: Int, @Path("topicId") topicId: Int): Observable<ResponseBody>

  @GET("/members/member{memberId}.html")
  fun loadUserInfo(@Path("memberId") memberId: Int): Observable<ResponseBody>
}