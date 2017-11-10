package com.sedsoftware.yaptalker.data.requests.thumbnails

import com.sedsoftware.yaptalker.data.video.CoubData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CoubLoader {
  @GET("/api/oembed.json")
  fun loadThumbnail(
      @Query("url") url: String): Single<CoubData>
}
