package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

interface ThumbnailRepository {

  fun getThumbnail(videoLink: String): Single<String>
}
