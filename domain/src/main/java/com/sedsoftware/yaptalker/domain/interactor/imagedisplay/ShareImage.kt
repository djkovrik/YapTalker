package com.sedsoftware.yaptalker.domain.interactor.imagedisplay

import com.sedsoftware.yaptalker.domain.device.SharingHelper
import com.sedsoftware.yaptalker.domain.interactor.CompletableUseCaseWithParameter
import io.reactivex.Completable
import javax.inject.Inject

class ShareImage @Inject constructor(
  private val sharingHelper: SharingHelper
) : CompletableUseCaseWithParameter<ShareImage.Params> {

  override fun execute(parameter: Params): Completable =
    sharingHelper
      .shareImage(parameter.url)

  class Params(val url: String)
}
