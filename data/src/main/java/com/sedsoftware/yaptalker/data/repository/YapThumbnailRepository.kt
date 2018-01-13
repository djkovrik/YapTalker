package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.repository.thumbnailsource.ThumbnailSourceFactory
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapThumbnailRepository @Inject constructor(
    private val thumbnailSourceFactory: ThumbnailSourceFactory
) : ThumbnailRepository {

  override fun getThumbnail(videoLink: String): Observable<String> =
      thumbnailSourceFactory
          .createThumbnailSource(videoLink)
          .getThumbnailUrl()
}
