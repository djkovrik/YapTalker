package com.sedsoftware.yaptalker.data.network.thumbnails

import com.sedsoftware.yaptalker.data.thumbnail.CoubData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CoubLoader {

  @GET("/api/oembed.json")
  fun loadThumbnail(
      @Query("url") url: String): Observable<CoubData>
}
