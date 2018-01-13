package com.sedsoftware.yaptalker.domain.interactor.old

import com.sedsoftware.yaptalker.domain.interactor.old.GetVideoThumbnail.Params
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetVideoThumbnail @Inject constructor(
    private val thumbnailRepository: ThumbnailRepository
) : UseCaseOld<String, Params> {

  override fun buildUseCaseObservable(params: Params): Observable<String> =
      thumbnailRepository
          .getThumbnail(params.link)

  class Params(val link: String)
}
