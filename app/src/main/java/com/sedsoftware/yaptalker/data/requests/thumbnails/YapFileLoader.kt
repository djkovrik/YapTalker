package com.sedsoftware.yaptalker.data.requests.thumbnails

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface YapFileLoader {
  @GET("/get_player")
  fun loadHash(@Query("v") v: String): Single<String>
}
