package com.sedsoftware.yaptalker.domain.interactor.common

import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import io.reactivex.Single
import javax.inject.Inject

class GetVideoThumbnail @Inject constructor(
    private val thumbnailRepository: ThumbnailRepository
) : SingleUseCaseWithParameter<GetVideoThumbnail.Params, String> {

  override fun execute(parameter: Params): Single<String> =
      thumbnailRepository
          .getThumbnail(parameter.link)

  class Params(val link: String)
}
