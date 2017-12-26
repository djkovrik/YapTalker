package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetVideoThumbnail @Inject constructor(
    private val thumbnailRepository: ThumbnailRepository
) : UseCase<String, GetVideoThumbnail.Params> {

  override fun buildUseCaseObservable(params: Params): Observable<String> =
      thumbnailRepository
          .getThumbnail(params.link)

  class Params(val link: String)
}
