package com.sedsoftware.yaptalker.domain.interactor.imagedisplay

import com.sedsoftware.yaptalker.domain.device.ImageStorage
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCaseWithParameter
import io.reactivex.Single
import javax.inject.Inject

class SaveImage @Inject constructor(
  private val imageStorage: ImageStorage
) : SingleUseCaseWithParameter<SaveImage.Params, String> {

  override fun execute(parameter: Params): Single<String> =
    imageStorage
      .saveImage(parameter.url)

  class Params(val url: String)
}
