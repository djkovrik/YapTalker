package com.sedsoftware.yaptalker.data.repository.thumbnail

import io.reactivex.Single

interface ThumbnailSource {

  fun getThumbnailUrl(): Single<String>
}
