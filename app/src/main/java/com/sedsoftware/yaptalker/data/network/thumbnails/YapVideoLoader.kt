package com.sedsoftware.yaptalker.data.network.thumbnails

import com.sedsoftware.yaptalker.data.thumbnail.YapVideoData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YapVideoLoader {

  @GET("/load/{id}")
  fun loadThumbnail(
      @Path("id") id: String,
      @Query("md5") md5: String,
      @Query("type") type: String): Observable<YapVideoData>
}
