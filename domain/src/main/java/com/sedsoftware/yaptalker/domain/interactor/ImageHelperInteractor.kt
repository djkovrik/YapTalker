package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.device.ImageStorage
import com.sedsoftware.yaptalker.domain.device.SharingHelper
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ImageHelperInteractor @Inject constructor(
    private val imageStorage: ImageStorage,
    private val sharingHelper: SharingHelper
) {

    fun saveImage(url: String): Single<String> =
        imageStorage
            .saveImage(url)

    fun shareImage(url: String): Completable =
        sharingHelper
            .shareImage(url)
}
