package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.repository.thumbnail.ThumbnailSourceFactory
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import io.reactivex.Single
import javax.inject.Inject

class YapThumbnailRepository @Inject constructor(
  private val thumbnailSourceFactory: ThumbnailSourceFactory
) : ThumbnailRepository {

  override fun getThumbnail(videoLink: String): Single<String> =
    thumbnailSourceFactory
      .createThumbnailSource(videoLink)
      .getThumbnailUrl()
}
