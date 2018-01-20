package com.sedsoftware.yaptalker.data.repository.thumbnail

import com.sedsoftware.yaptalker.data.network.thumbnails.RutubeLoader
import io.reactivex.Single

class RutubeThumbnailSource(
  private val rutubeLoader: RutubeLoader,
  private val videoLink: String
) : ThumbnailSource {

  private val videoId: String by lazy {
    videoLink.substringAfterLast("/")
  }

  override fun getThumbnailUrl(): Single<String> =
    rutubeLoader
      .loadThumbnail(id = videoId, format = "json")
      .map { it.thumbnail_url }
}
