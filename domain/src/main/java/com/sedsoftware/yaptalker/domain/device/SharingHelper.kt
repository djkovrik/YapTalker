package com.sedsoftware.yaptalker.domain.device

import io.reactivex.Completable

interface SharingHelper {

  fun shareImage(url: String): Completable
}
