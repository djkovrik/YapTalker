package com.sedsoftware.yaptalker.data.network.thumbnails

import com.sedsoftware.yaptalker.data.repository.thumbnail.data.VkResponseWrapper
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface VkLoader {

  @GET("/method/video.get")
  fun loadThumbnail(
      @Query("videos") videos: String,
      @Query("access_token") access_token: String,
      @Query("v") version: String
  ): Observable<VkResponseWrapper>
}
