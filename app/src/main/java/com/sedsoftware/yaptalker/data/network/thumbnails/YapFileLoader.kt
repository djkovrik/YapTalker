package com.sedsoftware.yaptalker.data.network.thumbnails

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface YapFileLoader {

  @GET("/get_player")
  fun loadHash(
      @Query("v") v: String): Observable<String>
}
