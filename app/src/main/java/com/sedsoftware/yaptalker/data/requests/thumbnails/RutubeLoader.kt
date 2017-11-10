package com.sedsoftware.yaptalker.data.requests.thumbnails

import com.sedsoftware.yaptalker.data.video.RutubeData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RutubeLoader {
  @GET("/api/video/{id}")
  fun loadThumbnail(
      @Path("id") id: String, @Query("format") format: String): Single<RutubeData>
}
