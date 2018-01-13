package com.sedsoftware.yaptalker.data.repository.thumbnailsource

import com.sedsoftware.yaptalker.data.network.thumbnails.YapFileLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.YapVideoLoader
import com.sedsoftware.yaptalker.data.thumbnail.YapVideoData
import io.reactivex.Observable

class YapThumbnailSource(
    private val yapFileLoader: YapFileLoader,
    private val yapVideoLoader: YapVideoLoader,
    private val videoLink: String
) : ThumbnailSource {

  companion object {
    private const val YAP_RESULT_TYPE = "json"
  }

  private val videoId: String by lazy {
    videoLink.substringAfterLast("=")
  }

  override fun getThumbnailUrl(): Observable<String> =
      yapFileLoader
          .loadHash(videoId)
          .flatMap { hash: String -> getYapPlayerObservable(videoId, hash) }


  private fun getYapPlayerObservable(id: String, hash: String): Observable<String> =
      yapVideoLoader
          .loadThumbnail(id, hash, YAP_RESULT_TYPE)
          .map { yapInfo: YapVideoData -> yapInfo.player.poster }
}
