package com.sedsoftware.yaptalker.domain.device

import io.reactivex.Single

interface ImageStorage {

    fun saveImage(url: String): Single<String>
}
