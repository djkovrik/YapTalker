package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import io.reactivex.Single
import javax.inject.Inject

class VideoThumbnailsInteractor @Inject constructor(
  private val thumbnailRepository: ThumbnailRepository
) {

  fun getThumbnail(link: String): Single<String> =
    thumbnailRepository
      .getThumbnail(link)
}
