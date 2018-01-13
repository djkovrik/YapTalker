package com.sedsoftware.yaptalker.data.network.thumbnails

import com.sedsoftware.yaptalker.data.repository.thumbnail.data.RutubeData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RutubeLoader {

  @GET("/api/video/{id}")
  fun loadThumbnail(@Path("id") id: String, @Query("format") format: String): Observable<RutubeData>
}
