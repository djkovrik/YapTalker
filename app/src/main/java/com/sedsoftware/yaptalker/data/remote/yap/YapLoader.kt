@file:Suppress("KDocUnresolvedReference")

package com.sedsoftware.yaptalker.data.remote.yap

import com.sedsoftware.yaptalker.data.model.Forums
import com.sedsoftware.yaptalker.data.model.News
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

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
}
