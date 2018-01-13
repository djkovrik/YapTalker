package com.sedsoftware.yaptalker.data.repository.thumbnail

import com.sedsoftware.yaptalker.data.network.thumbnails.RutubeLoader
import com.sedsoftware.yaptalker.data.repository.thumbnail.data.RutubeData
import io.reactivex.Observable

class RutubeThumbnailSource(
    private val rutubeLoader: RutubeLoader,
    private val videoLink: String
) : ThumbnailSource {

  private val videoId: String by lazy {
    videoLink.substringAfterLast("/")
  }

  override fun getThumbnailUrl(): Observable<String> =
      rutubeLoader
          .loadThumbnail(id = videoId, format = "json")
          .map { rutubeInfo: RutubeData -> rutubeInfo.thumbnail_url }
}
