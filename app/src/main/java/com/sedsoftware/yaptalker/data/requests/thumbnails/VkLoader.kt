package com.sedsoftware.yaptalker.data.requests.thumbnails

import com.sedsoftware.yaptalker.data.video.VkResponseWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface VkLoader {
  @GET("/method/video.get")
  fun loadThumbnail(
      @Query("videos") videos: String,
      @Query("access_token") access_token: String,
      @Query("v") version: String): Single<VkResponseWrapper>
}
