package com.sedsoftware.yaptalker.data.remote.thumbnails

import com.sedsoftware.yaptalker.data.model.video.CoubData
import com.sedsoftware.yaptalker.data.model.video.RutubeData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RutubeThumbnailLoader {
  @GET("/api/video/{id}")
  fun loadThumbnail(@Path("id") id: String, @Query("format") format: String) : Single<RutubeData>
}

interface CoubThumbnailLoader {
  @GET("/api/oembed.json")
  fun loadThumbnail(@Query("url") url: String) : Single<CoubData>
}
