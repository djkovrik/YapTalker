package com.sedsoftware.yaptalker.data.repository.thumbnailsource

import com.sedsoftware.yaptalker.data.network.thumbnails.CoubLoader
import com.sedsoftware.yaptalker.data.thumbnail.CoubData
import io.reactivex.Observable

class CoubThumbnailSource(
    private val coubLoader: CoubLoader,
    private val videoLink: String
) : ThumbnailSource {

  private val videoId: String by lazy {
    videoLink.substringAfterLast("/")
  }

  override fun getThumbnailUrl(): Observable<String> =
      coubLoader
          .loadThumbnail("http://coub.com/view/$videoId")
          .map { coubInfo: CoubData -> coubInfo.thumbnail_url }
}
